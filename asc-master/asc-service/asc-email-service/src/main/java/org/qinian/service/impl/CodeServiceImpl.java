package org.qinian.service.impl;

import lombok.RequiredArgsConstructor;
import org.qinian.service.CodeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 随机生成指定长度字符串验证码
     *
     * @param length 验证码长度
     */
    public String generateVerifyCode(String email, int length) {
        String strRange = "1234567890";
        StringBuilder strBuilder = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            char ch = strRange.charAt((new Random()).nextInt(strRange.length()));
            strBuilder.append(ch);
        }

        // 设置redis
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String msgKey = "msg_" + email;
        valueOperations.set(msgKey, strBuilder.toString());

        return strBuilder.toString();
    }

    /**
     * 校验验证码（可用作帐号登录、注册、修改信息等业务）
     * 校验验证码：0验证码错误、1验证码正确、2验证码过期
     */
    public Integer checkVerifyCode(String email, String code) {
        int result = 1;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String msgKey = "msg_" + email;
        String verifyCode = (String) valueOperations.get(msgKey);

        /*校验验证码：0验证码错误、1验证码正确、2验证码过期*/
        if (verifyCode == null) {
            result = 2;
        } else if (!code.equals(verifyCode)) {
            result = 0;
        }
        // 如果验证码正确，则从redis删除
        if (result == 1) {
            redisTemplate.delete(msgKey);
        }
        return result;
    }
}
