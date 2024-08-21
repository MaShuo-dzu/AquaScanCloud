package org.qinian.utils;

import com.alibaba.fastjson.JSONObject;
import feign.template.UriUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocationUtil {
    private static final String AK = "qrqd4LoWwDjVC2Ewt86uEEkR9r6ZdwHZ";

    /**
     * 返回adcode
     *
     * @param lat 纬度
     * @param lon 经度
     * @return adcode
     */
    public static String getAdcodeByLocation(Double lon, Double lat) {
        // request
        BufferedReader reader;
        StringBuilder sbf = new StringBuilder();

        String location = UriUtils.encode(lat + "," + lon);
        // sn
//        String httpUrl = "https://api.map.baidu.com/reverse_geocoding/v3?ak=" + AK + "&output=json&coordtype=wgs84ll&extensions_poi=0&location="+location + "&sn=" + caculateSn(location);
        // 白名单
        String httpUrl = "https://api.map.baidu.com/reverse_geocoding/v3?ak=" + AK + "&output=json&coordtype=wgs84ll&extensions_poi=0&location=" + location;
        try {
            // 1.请求
            URL url = new URL(httpUrl);
            URLConnection httpConnection = url.openConnection();
            httpConnection.connect();

            InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                sbf.append(line);
            }
            reader.close();
            isr.close();

            // 2.解析数据
            JSONObject object = JSONObject.parseObject(sbf.toString());
            String status = object.get("status").toString();
            if (status.equals("0")) {
                JSONObject resultResponse = (JSONObject) object.get("result");
                JSONObject addressComponent = (JSONObject) resultResponse.get("addressComponent");
                return (String) addressComponent.get("adcode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String caculateSn(String location) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("ak", AK);
        paramsMap.put("output", "json");
        paramsMap.put("coordtype", "wgs84ll");
        paramsMap.put("extensions_poi", "0");
        paramsMap.put("location", location);


        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = toQueryString(paramsMap);

        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
        String SK = "PEi1kdkeOFd5qF9d2BKzPzwdRiAfKiQW";
        String wholeStr = "/reverse_geocoding/v3?" + paramsStr + SK;

        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, StandardCharsets.UTF_8);

        // 调用下面的MD5方法得到最后的sn签名
        return MD5(tempStr);
    }

    // 对Map内所有value作utf8编码，拼接返回结果
    private static String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey()).append("=");
            //    第一种方式使用的 jdk 自带的转码方式  第二种方式使用的 spring 的转码方法 两种均可
            queryString.append(URLEncoder.encode((String) pair.getValue(), StandardCharsets.UTF_8).replace("+", "%20")).append("&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    private static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(getAdcodeByLocation(116.2241399, 37.39459878));
    }
}
