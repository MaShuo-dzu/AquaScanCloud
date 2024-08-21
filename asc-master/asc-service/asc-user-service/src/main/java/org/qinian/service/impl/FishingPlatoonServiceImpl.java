package org.qinian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.qinian.dao.FishingPlatoonMapper;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonLoginFormDTO;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonRegisterFormDTO;
import org.qinian.domain.pojo.FishingPlatoon;
import org.qinian.domain.vo.UserLoginVO;
import org.qinian.enums.UserStatus;
import org.qinian.exception.BadRequestException;
import org.qinian.exception.ForbiddenException;
import org.qinian.properties.JwtProperties;
import org.qinian.service.IFishingPlatoonService;
import org.qinian.utils.AESUtil;
import org.qinian.utils.Base64Util;
import org.qinian.utils.JwtTool;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Service
@RequiredArgsConstructor
public class FishingPlatoonServiceImpl extends ServiceImpl<FishingPlatoonMapper, FishingPlatoon> implements IFishingPlatoonService {
    private final RabbitTemplate rabbitTemplate;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    @Override
    public UserLoginVO login(FishingPlatoonLoginFormDTO fishingPlatoonLoginFormDTO) throws Exception {
        // 1.数据校验
        String username = fishingPlatoonLoginFormDTO.getUsername();
        String password = Base64Util.encode(AESUtil.encryptAndDecrypt(fishingPlatoonLoginFormDTO.getPassword().getBytes(StandardCharsets.UTF_8), 1));

        // 2.根据用户名或手机号查询
        FishingPlatoon user = lambdaQuery().eq(FishingPlatoon::getUsername, username).one();
        if (user == null) {
            System.out.println("用户不存在");
            return null;
        }

        // 3.校验是否禁用
        if (user.getState() == UserStatus.FROZEN) {
            System.out.println("用户被禁用");
            return null;
        }

        // 4.校验密码
        if (!password.equals(user.getPassword())) {
            System.out.println("密码错误");
            return null;
        }

        // 5.生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());

        // 6. 登录时间计算
        user.setLoginTime(LocalDateTime.now());
        baseMapper.updateById(user);

        // 7.封装VO返回
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);
        return vo;
    }

    @Override
    public Boolean register(FishingPlatoonRegisterFormDTO fishingPlatoonRegisterFormDTO) {
        // 1. 检测用户名重复
        if (baseMapper.selectCount(new QueryWrapper<FishingPlatoon>().eq("username", fishingPlatoonRegisterFormDTO.getUsername())) > 0) {
            System.out.println("检测用户名重复");
            return false;
        }

        // 2. 检测邮箱重复
        if (baseMapper.selectCount(new QueryWrapper<FishingPlatoon>().eq("email", fishingPlatoonRegisterFormDTO.getEmail())) > 0) {
            System.out.println("检测邮箱重复");
            return false;
        }

        // 3. 检测手机号重复
        if (baseMapper.selectCount(new QueryWrapper<FishingPlatoon>().eq("phone", fishingPlatoonRegisterFormDTO.getPhone())) > 0) {
            System.out.println("检测手机号重复");
            return false;
        }

        // 4. 检测营业许可的图像编码合法性
        if (!Base64Util.isValidBase64(fishingPlatoonRegisterFormDTO.getBlImageBase())) {
            System.out.println("检测营业许可的图像编码不合法");
            return false;
        }

        // 5. mq处理 图像编码 （检测是否合格 -> 注册成功 [信息保存] -> 发送邮箱）
        rabbitTemplate.convertAndSend("register.topic", "register.success", fishingPlatoonRegisterFormDTO);

        return true;
    }

    @Override
    public Boolean updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception {
        // 1. 检查用户是否存在
        FishingPlatoon fishingPlatoon = baseMapper.selectById(updatePWDDTO.getUserId());
        if (fishingPlatoon == null) {
            return false;
        }

        // 2. 验证旧密码是否正确
        String encryptedOldPassword = Base64Util.encode(AESUtil.encryptAndDecrypt(updatePWDDTO.getOldPassword().getBytes(StandardCharsets.UTF_8), 1));
        if (!fishingPlatoon.getPassword().equals(encryptedOldPassword)) {
            return false;
        }

        // 3. 更新新密码
        String encryptedNewPassword = Base64Util.encode(AESUtil.encryptAndDecrypt(updatePWDDTO.getNewPassword().getBytes(StandardCharsets.UTF_8), 1));
        fishingPlatoon.setPassword(encryptedNewPassword);
        int updateResult = baseMapper.updateById(fishingPlatoon);

        return updateResult > 0;
    }

    @Override
    public FishingPlatoon selectByInvitationKey(String invitationKey) {
        return baseMapper.selectByInvitationCode(invitationKey);
    }

    @Override
    public FishingPlatoon selectByEmail(String email) {
        return baseMapper.selectOne(new QueryWrapper<FishingPlatoon>().eq("email", email));
    }
}
