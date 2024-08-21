package org.qinian.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;


public class AESUtil {
    private static String appsecret = MD5.md5("mashuo@dzu@12");

    /****
     * AES加密/解密
     * @param buffer:密文/明文
     * @param mode:加密/解密模式  1 加密  2 解密
     * @return
     */
    public static byte[] encryptAndDecrypt(byte[] buffer, Integer mode) throws Exception {
        //1:加载加密解密算法处理对象（包含算法、秘钥管理）
        Security.addProvider(new BouncyCastleProvider());

        //2:根据不同算法创建秘钥   1）秘钥的字节数组   2）加密算法
        SecretKeySpec secretKeySpec = new SecretKeySpec(appsecret.getBytes(StandardCharsets.UTF_8), "AES");

        //3:设置加密模式（无论是加密还是解析，模式一致）
        // 1): AES/ECB/KPS7Padding 设置算法
        // 2):指定算法库对象
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        //4:初始化加密配置
        cipher.init(mode, secretKeySpec);
        //5:执行加密/解密
        return cipher.doFinal(buffer);
    }

    public static void main(String[] args) throws Exception {
        String password = "123456";
        String encode = Base64Util.encode(AESUtil.encryptAndDecrypt(password.getBytes(StandardCharsets.UTF_8), 1));
        System.out.println(encode);
    }
}
