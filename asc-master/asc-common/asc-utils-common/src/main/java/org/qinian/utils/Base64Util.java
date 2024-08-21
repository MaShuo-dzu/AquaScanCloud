package org.qinian.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

public class Base64Util {

    /***
     * 普通解密操作
     * @param encodedText：密文
     * @return
     */
    public static byte[] decode(String encodedText) {
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }

    /***
     * 普通加密操作
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /***
     * 解密操作
     * @param encodedText
     * @return
     */
    public static byte[] decodeURL(String encodedText) {
        final Base64.Decoder decoder = Base64.getUrlDecoder();
        return decoder.decode(encodedText);
    }

    /***
     * 加密操作
     * @param data
     * @return
     */
    public static String encodeURL(byte[] data) {
        final Base64.Encoder encoder = Base64.getUrlEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * 判断是否是base64编码
     *
     * @param imageCode
     * @return
     */
    public static boolean isValidBase64(String imageCode) {
        if (imageCode == null || imageCode.isEmpty()) {
            return false;
        }
        try {
            // Decode the string to see if it is a valid Base64 encoded string
            Base64.getDecoder().decode(imageCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 图片的base编码
     *
     * @param imageFile 图片file格式
     * @return
     */
    public static String encodeImageToBase64(File imageFile, Boolean urlEncode) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        // 其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imageFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        if (!urlEncode) {
            final Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(data);
        }

        final Base64.Encoder encoder = Base64.getUrlEncoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encodeToString(data);
    }
}