package org.qinian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.qinian.dao.AdminMapper;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.admin.AdminLoginFormDTO;
import org.qinian.domain.pojo.Admin;
import org.qinian.domain.vo.UserLoginVO;
import org.qinian.exception.BadRequestException;
import org.qinian.properties.JwtProperties;
import org.qinian.service.IAdminService;
import org.qinian.utils.AESUtil;
import org.qinian.utils.Base64Util;
import org.qinian.utils.JwtTool;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    @Override
    public UserLoginVO login(AdminLoginFormDTO loginDTO) throws Exception {
        // 0. 系统默认唯一admin管理员，且无账号,且无法注册
        Admin user = baseMapper.selectOne(new QueryWrapper<Admin>().last("LIMIT 1"));

        // 1.数据校验
        String password = Base64Util.encode(AESUtil.encryptAndDecrypt(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8), 1));

        // 2.校验密码
        if (!password.equals(user.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }

        // 4.生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());

        // 5. 登录时间计算
        user.setLoginTime(LocalDateTime.now());
        baseMapper.updateById(user);

        // 6.封装VO返回
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(null);
        vo.setToken(token);

        return vo;
    }

    @Override
    public Boolean updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception {
        // 1. 检查用户是否存在
        Admin admin = baseMapper.selectById(updatePWDDTO.getUserId());
        if (admin == null) {
            return false;
        }

        // 2. 验证旧密码是否正确
        String encryptedOldPassword = Base64Util.encode(AESUtil.encryptAndDecrypt(updatePWDDTO.getOldPassword().getBytes(StandardCharsets.UTF_8), 1));
        if (!admin.getPassword().equals(encryptedOldPassword)) {
            return false;
        }

        // 3. 更新新密码
        String encryptedNewPassword = Base64Util.encode(AESUtil.encryptAndDecrypt(updatePWDDTO.getNewPassword().getBytes(StandardCharsets.UTF_8), 1));
        admin.setPassword(encryptedNewPassword);
        int updateResult = baseMapper.updateById(admin);

        return updateResult > 0;
    }
}
