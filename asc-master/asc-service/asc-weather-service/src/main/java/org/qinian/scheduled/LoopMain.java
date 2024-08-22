package org.qinian.scheduled;

import lombok.RequiredArgsConstructor;
import org.qinian.domain.dto.weather.WeatherAddDto;
import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.qinian.entity.WeatherInfo;
import org.qinian.model.RemoteWeatherService;
import org.qinian.utils.LocationUtil;
import org.qinian.utils.WeatherUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Vector;

/**
 * 天气监测api有配额！
 * @author qinian
 */
@Component
@RequiredArgsConstructor
public class LoopMain {
    private final Vector<WeatherLoopAddDto> threadSafeVector;

    private final RemoteWeatherService remoteWeatherService;

//    @Scheduled(cron = "0 0 10 * * ?")  // 每天早上10点执行一次
//    @Scheduled(cron = "*0* * * * ?")   // 每分钟执行一次
    @Scheduled(cron = "*00 * * * ?")   // 每小时执行一次
//    @Scheduled(cron = "* * * * * ?")   // 每秒钟执行一次
    public void loopMainTask() throws Exception {
        for (WeatherLoopAddDto weatherLoopAddDto : threadSafeVector) {
            // 1.获取城市adcode
            String adcode = LocationUtil.getAdcodeByLocation(
                    weatherLoopAddDto.getLongitude(),
                    weatherLoopAddDto.getLatitude());

            // 1.1 adcode 为6位数
            if (adcode.length() != 6) {
                continue;
            }

            // 2.获取adcode对应的天气信息
            WeatherInfo weatherInfo = WeatherUtil.getWeatherInfo(adcode);

            // 2.1 判断有没有天气信息
            if (weatherInfo.getLives().length == 0) {
                continue;
            }

            // 3.创建天气信息
            WeatherAddDto weatherAddDto = new WeatherAddDto(
                    weatherLoopAddDto.getFishRaftId(),
                    weatherLoopAddDto.getLocationId(),
                    weatherInfo.getLives()[0]  // 只有一个值
            );

            // 4.保存数据库
            remoteWeatherService.createWeatherInfo(weatherAddDto);
        }
    }
}
