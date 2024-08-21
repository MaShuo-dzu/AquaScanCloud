package org.qinian.controller;

import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.springframework.web.bind.annotation.*;

import java.util.Vector;

@RestController
@RequestMapping("/api/weather-monitor/loop")
@RequiredArgsConstructor
public class LoopMainController {
    private final Vector<WeatherLoopAddDto> threadSafeVector;

    @PostMapping("/add")
    public Result addRaft(@RequestBody WeatherLoopAddDto weatherLoopAddDto) {
        threadSafeVector.add(weatherLoopAddDto);

        return Result.success(200, "ok", null);
    }

    /**
     * 渔排点删除监控
     *
     * @param id 渔排点id
     * @return
     */
    @GetMapping("/delete")
    public Result deleteRaft(@RequestBody Long id) {
        threadSafeVector.removeIf(weatherLoopAddDto -> weatherLoopAddDto.getFishRaftId().equals(id));

        return Result.success(200, "ok", null);
    }
}
