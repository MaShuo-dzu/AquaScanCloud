package org.qinian.listener;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.qinian.domain.dto.email.SendSimpleEmailDto;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonRegisterFormDTO;
import org.qinian.domain.pojo.BusinessLicense;
import org.qinian.domain.pojo.FishingPlatoon;
import org.qinian.entity.QCCInfo;
import org.qinian.enums.UserStatus;
import org.qinian.model.RemoteEmailService;
import org.qinian.service.IBusinessLicenseService;
import org.qinian.service.IFishingPlatoonService;
import org.qinian.utils.AESUtil;
import org.qinian.utils.Base64Util;
import org.qinian.utils.UniqueInviteCodeUtil;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FPRegisterListener {
    private final IBusinessLicenseService businessLicenseService;

    private final IFishingPlatoonService fishingPlatoonService;

    private final RemoteEmailService remoteEmailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "register.success.queue", durable = "true"),
            exchange = @Exchange(name = "register.topic"),
            key = "register.success"
    ))
    public void listenRegisterSuccess(FishingPlatoonRegisterFormDTO fishingPlatoonRegisterFormDTO) throws Exception {
        // 1.检测 businessLicense 是否合法 （关闭）
//        QCCInfo bl = businessLicenseService.checkBL(fishingPlatoonRegisterFormDTO.getBlImageBase());
//
//        if (bl == null) {
//            // 1.1 注册失败
//            remoteEmailService.sendSimpleEmail(
//                    new SendSimpleEmailDto(
//                            fishingPlatoonRegisterFormDTO.getEmail(),
//                            "注册失败",
//                            "注册失败，请重新注册"
//                    )
//            );
//        }

        // 2. 保存营业执照
        // 2.1 图片aes加密
        String imageBase = Base64Util.encode(AESUtil.encryptAndDecrypt(fishingPlatoonRegisterFormDTO.getBlImageBase().getBytes(StandardCharsets.UTF_8), 1));
        BusinessLicense businessLicense = new BusinessLicense();
        businessLicense.setBusinessLicense(imageBase);
        businessLicense.setLicenseNumber("");  // TODO
        businessLicense.setLicenseCreateTime(LocalDate.now());
        businessLicense.setLicenseEffectiveTime(LocalDate.now());  // TODO
        businessLicenseService.save(businessLicense);

        // 3. 注册
        FishingPlatoon fishingPlatoon = BeanUtil.copyProperties(fishingPlatoonRegisterFormDTO, FishingPlatoon.class);
        String password = Base64Util.encode(AESUtil.encryptAndDecrypt(fishingPlatoonRegisterFormDTO.getPassword().getBytes(StandardCharsets.UTF_8), 1));
        fishingPlatoon.setBusinessLicenseId(businessLicense.getId());
        fishingPlatoon.setPassword(password);  // 加密
        fishingPlatoon.setCreateTime(LocalDateTime.now());
        fishingPlatoon.setUpdateTime(LocalDateTime.now());
        fishingPlatoon.setState(UserStatus.NORMAL);
        fishingPlatoon.setInvitationCode(UniqueInviteCodeUtil.generateInviteCode(fishingPlatoonRegisterFormDTO.getUsername()));

        fishingPlatoonService.save(fishingPlatoon);

        // 4. 注册成功
        remoteEmailService.sendSimpleEmail(
                new SendSimpleEmailDto(
                        fishingPlatoonRegisterFormDTO.getEmail(),
                        "注册成功",
                        "注册成功，请登录"
                )
        );
    }
}
