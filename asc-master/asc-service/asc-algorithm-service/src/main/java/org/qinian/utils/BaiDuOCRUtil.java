package org.qinian.utils;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.qinian.properties.BaiduProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BaiDuOCRUtil {
    private final BaiduProperties baiduProperties;

    /**
     * 获取token
     *
     * @return token
     */
    private String getAuth() {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + baiduProperties.getAk()
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + baiduProperties.getSk();

        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            //百度推荐使用POST请求
            connection.setRequestMethod("POST");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
//            System.out.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            System.err.print("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }


    /**
     * 调用OCR
     *
     * @param httpUrl
     * @param httpArg
     * @return
     */
    public String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            //用java JDK自带的URL去请求
            URL url = new URL(httpUrl + "?access_token=" + getAuth());
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            //设置该请求的消息头
            //设置HTTP方法：POST
            connection.setRequestMethod("POST");
            //设置其Header的Content-Type参数为application/x-www-form-urlencoded
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result:" + result);
        return result;
    }

    public Boolean checkAvailable(String jsonResult) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            return !jsonObject.has("error_code");  // 如果没有error_code，则表示请求成功
        } catch (Exception e) {
            // 处理异常，例如JSON格式错误
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 营业执照参数转换
     *
     * @param jsonResult request return
     * @return BL map
     */
    public HashMap<String, String> getBusinessLicHashMap(String jsonResult) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONObject words_result = jsonObject.getJSONObject("words_result");
            Iterator<String> it = words_result.keys();
            while (it.hasNext()) {
                String key = it.next();
                JSONObject result = words_result.getJSONObject(key);
                String value = result.getString("words");
                switch (key) {
                    case "单位名称" -> map.put("单位名称", value);
                    case "类型" -> map.put("类型", value);
                    case "法人" -> map.put("法人", value);
                    case "地址" -> map.put("地址", value);
                    case "有效期" -> map.put("有效期", value);
                    case "性别" -> map.put("性别", value);
                    case "证件编号" -> map.put("证件编号", value);
                    case "签发日期" -> map.put("签发日期", value);
                    case "社会信用代码" -> map.put("社会信用代码", value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 身份证参数转换
     *
     * @param jsonResult
     * @return
     */
    public HashMap<String, String> getIdCardHashMap(String jsonResult) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONObject words_result = jsonObject.getJSONObject("words_result");
            Iterator<String> it = words_result.keys();
            while (it.hasNext()) {
                String key = it.next();
                JSONObject result = words_result.getJSONObject(key);
                String value = result.getString("words");
                switch (key) {
                    case "姓名" -> map.put("姓名", value);
                    case "民族" -> map.put("民族", value);
                    case "住址" -> map.put("住址", value);
                    case "公民身份号码" -> map.put("公民身份号码", value);
                    case "出生" -> map.put("出生日期", value);
                    case "性别" -> map.put("性别", value);
                    case "失效日期" -> map.put("失效日期", value);
                    case "签发日期" -> map.put("签发日期", value);
                    case "签发机关" -> map.put("签发机关", value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
