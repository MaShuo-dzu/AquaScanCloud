package org.qinian.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.qinian.entity.WeatherInfo;

/**
 * 基于高德地图的api
 */
public class WeatherUtil {

    private static final String WEATHER_API_URL = "https://restapi.amap.com/v3/weather/weatherInfo";
    private static final String KEY = "c306b59b0342eb1190612f0584aa1f56";

    public static WeatherInfo getWeatherInfo(String adcode) throws Exception {
        // 创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HttpGet对象
        HttpGet request = new HttpGet(WEATHER_API_URL + "?city=" + adcode + "&key=" + KEY);
        // 发送请求并获取响应
        HttpResponse response = httpClient.execute(request);
        // 读取响应内容
        String result = EntityUtils.toString(response.getEntity());
//        System.out.println(result);
        // 关闭HttpClient连接
        httpClient.close();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, WeatherInfo.class);
    }

    public static void main(String[] args) throws Exception {
        WeatherInfo weatherInfo = getWeatherInfo("131126");
        // Accessing data
        for (WeatherInfo.Live live : weatherInfo.getLives()) {
            System.out.println("Province: " + live.getProvince());
            System.out.println("City: " + live.getCity());
            System.out.println("Adcode: " + live.getAdcode());
            System.out.println("Weather: " + live.getWeather());
            System.out.println("Temperature: " + live.getTemperature());
            System.out.println("Wind Direction: " + live.getWindDirection());
            System.out.println("Wind Power: " + live.getWindPower());
            System.out.println("Humidity: " + live.getHumidity());
            System.out.println("Report Time: " + live.getReportTime());
        }
    }
}
