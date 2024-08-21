package org.qinian.scheduled;

import lombok.RequiredArgsConstructor;
import org.qinian.domain.dto.weather.WeatherAddDto;
import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.qinian.entity.WeatherInfo;
import org.qinian.model.RemoteAlertService;
import org.qinian.model.RemoteWeatherService;
import org.qinian.utils.LocationUtil;
import org.qinian.utils.WeatherUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Vector;

@Component
@RequiredArgsConstructor
public class LoopMain {
    private final Vector<WeatherLoopAddDto> threadSafeVector;

    private final RemoteWeatherService remoteWeatherService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void loopMainTask() throws Exception {
        for (WeatherLoopAddDto weatherLoopAddDto : threadSafeVector) {
            // 1.获取城市adcode
            String adcode = LocationUtil.getAdcodeByLocation(
                    weatherLoopAddDto.getLongitude(),
                    weatherLoopAddDto.getLatitude());

            // 2.获取adcode对应的天气信息
            WeatherInfo weatherInfo = WeatherUtil.getWeatherInfo(adcode);

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
