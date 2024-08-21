package org.qinian.model;

import org.qinian.domain.Result;
import org.qinian.domain.dto.weather.WeatherAddDto;
import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("weather-service")
public interface RemoteWeatherService {
    @PostMapping("/api/weather-monitor/loop/add")
    Result addRaft(@RequestBody WeatherLoopAddDto weatherLoopAddDto);

    @GetMapping("/api/weather-monitor/loop/delete")
    Result deleteRaft(@RequestParam Long id);

    @PostMapping("/weather-monitor")
    Result createWeatherInfo(@RequestBody WeatherAddDto weatherAddDto);
}
