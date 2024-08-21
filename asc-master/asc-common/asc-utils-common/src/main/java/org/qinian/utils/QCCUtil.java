package org.qinian.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.qinian.entity.BusinessLicInfo;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class QCCUtil {
    private static final String key = "570131fb14a543ab96ca08f61891e169";
    private static final String secret = "99F1E778C654B4E1A3AF8AEF64EDB063";

    private static String getUrl(BusinessLicInfo businessLicInfo) {

        return "http://api.qichacha.com/ECIMatch/CompanyVerify?key=" + key + "&dtype=json" + "&regNo=" + businessLicInfo.getCreditId()
                + "&companyName=" + businessLicInfo.getCompanyName() + "&frname=" + businessLicInfo.getCeo();
    }

    public static String request(BusinessLicInfo businessLicInfo) {
        String url = getUrl(businessLicInfo);

        int timestamp = DateUtil.getSecondTimestamp(new Date());
        String token = MD5.md5(key + timestamp + secret);

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);


            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            //添加header 头
            httpGet.setHeader("Token", token);
            httpGet.setHeader("Timespan", String.valueOf(timestamp));
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static Boolean check(String resultString) {
        try {
            JSONObject jsonObject = new JSONObject(resultString);
            // 检查"Result"键对应的值是否为null
            if (jsonObject.isNull("Result")) {
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            // 处理异常，例如JSON格式错误
            e.printStackTrace();
            return false;
        }
    }
}
