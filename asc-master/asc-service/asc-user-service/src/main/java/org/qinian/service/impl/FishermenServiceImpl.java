package org.qinian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.qinian.dao.FishermenMapper;
import org.qinian.dao.FishingPlatoonMapper;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.fishermen.FishermenLoginFormDTO;
import org.qinian.domain.dto.fishermen.FishermenRegisterFormDTO;
import org.qinian.domain.pojo.Fishermen;
import org.qinian.domain.pojo.FishingPlatoon;
import org.qinian.domain.vo.UserLoginVO;
import org.qinian.enums.UserStatus;
import org.qinian.exception.BadRequestException;
import org.qinian.exception.ForbiddenException;
import org.qinian.properties.JwtProperties;
import org.qinian.service.IFishermenService;
import org.qinian.utils.AESUtil;
import org.qinian.utils.Base64Util;
import org.qinian.utils.JwtTool;
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
public class FishermenServiceImpl extends ServiceImpl<FishermenMapper, Fishermen> implements IFishermenService {

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final FishingPlatoonMapper fishingPlatoonMapper;

    @Override
    public UserLoginVO login(FishermenLoginFormDTO loginDTO) throws Exception {
        // 1.数据校验
        String username = loginDTO.getUsername();
        String password = Base64Util.encode(AESUtil.encryptAndDecrypt(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8), 1));

        // 2.根据用户名或手机号查询
        Fishermen user = lambdaQuery().eq(Fishermen::getUsername, username).one();
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
    public Boolean register(FishermenRegisterFormDTO registerFormDTO) throws Exception {
        // 0.检查邀请码是否正确
        if (fishingPlatoonMapper.selectByInvitationCode(registerFormDTO.getInvitationCode()) == null) {
            System.out.println("邀请码不正确！");
            return false;
        }

        // 1. 检查用户名是否已存在
        Long usernameCount = baseMapper.selectCount(new QueryWrapper<Fishermen>()
                .eq("username", registerFormDTO.getUsername()));
        if (usernameCount > 0) {
            System.out.println("用户名已存在！");
            return false;
        }

        // 2. 检查邮箱是否已存在
        Long emailCount = baseMapper.selectCount(new QueryWrapper<Fishermen>()
                .eq("email", registerFormDTO.getEmail()));
        if (emailCount > 0) {
            System.out.println("邮箱已绑定！");
            return false;
        }

        // 3. 检查手机号是否已存在
        Long phoneCount = baseMapper.selectCount(new QueryWrapper<Fishermen>()
                .eq("phone", registerFormDTO.getPhone()));
        if (phoneCount > 0) {
            System.out.println("手机号已存在！");
            return false;
        }

        // 4.密码加密
        String password = Base64Util.encode(AESUtil.encryptAndDecrypt(registerFormDTO.getPassword().getBytes(StandardCharsets.UTF_8), 1));

        // 5. 注册新用户
        Fishermen fishermen = new Fishermen();
        fishermen.setUsername(registerFormDTO.getUsername());
        fishermen.setEmail(registerFormDTO.getEmail());
        fishermen.setPhone(registerFormDTO.getPhone());
        fishermen.setPassword(password);
        fishermen.setState(UserStatus.NORMAL);
        fishermen.setCreateTime(LocalDateTime.now());
        fishermen.setUpdateTime(LocalDateTime.now());

        // 6.邀请码（邀请码是唯一的）
        FishingPlatoon fishingPlatoon = fishingPlatoonMapper.selectByInvitationCode(registerFormDTO.getInvitationCode());
        fishermen.setFishingPlatoonId(fishingPlatoon.getId());

        int insertResult = baseMapper.insert(fishermen);
        return insertResult > 0;
    }

    @Override
    public Boolean updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception {
        // 1. 检查用户是否存在
        Fishermen fishermen = baseMapper.selectById(updatePWDDTO.getUserId());
        if (fishermen == null) {
            return false;
        }

        // 2. 验证旧密码是否正确
        String encryptedOldPassword = Base64Util.encode(AESUtil.encryptAndDecrypt(updatePWDDTO.getOldPassword().getBytes(StandardCharsets.UTF_8), 1));
        if (!fishermen.getPassword().equals(encryptedOldPassword)) {
            return false;
        }

        // 3. 更新新密码
        String encryptedNewPassword = Base64Util.encode(AESUtil.encryptAndDecrypt(updatePWDDTO.getNewPassword().getBytes(StandardCharsets.UTF_8), 1));
        fishermen.setPassword(encryptedNewPassword);
        int updateResult = baseMapper.updateById(fishermen);

        return updateResult > 0;
    }

    @Override
    public Fishermen selectByEmail(String email) {
        return baseMapper.selectOne(new QueryWrapper<Fishermen>().eq("email", email));
    }
}
