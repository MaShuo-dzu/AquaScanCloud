package org.qinian.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.weather.WeatherAddDto;
import org.qinian.domain.pojo.WeatherMonitor;
import org.qinian.service.IWeatherMonitorService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 天气实时检测数据的控制器
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/weather-monitor")
@RequiredArgsConstructor
public class WeatherMonitorController {
    private final IWeatherMonitorService weatherMonitorService;

    // 创建天气实时检测数据
    // 由LoopMainController调用
    @PostMapping
    public Result createWeatherInfo(@RequestBody WeatherAddDto weatherAddDto) {
        WeatherMonitor weatherMonitor = BeanUtil.copyProperties(weatherAddDto, WeatherMonitor.class);
        weatherMonitor.setCreateTime(LocalDateTime.now());

        boolean isSuccess = weatherMonitorService.save(weatherMonitor);
        if (isSuccess) {
            // 算法告警
            weatherMonitorService.alert(weatherMonitor);

            return Result.success(200, "天气实时检测数据创建成功", weatherMonitor);
        } else {
            return Result.fail(500, "创建天气实时检测数据失败", null);
        }
    }

    // 天气实时检测数据查询根据id
    @GetMapping("/{id}")
    public Result getWeatherInfoById(@PathVariable Long id) {
        WeatherMonitor weatherMonitor = weatherMonitorService.getById(id);
        if (weatherMonitor != null) {
            return Result.success(200, "天气实时检测数据获取成功", weatherMonitor);
        } else {
            return Result.fail(404, "天气实时检测数据不存在", null);
        }
    }

    // 查询所有天气实时检测数据
    @GetMapping
    public Result getAllWeatherInfos() {
        List<WeatherMonitor> weatherMonitorList = weatherMonitorService.list();
        return Result.success(200, "所有天气实时检测数据获取成功", weatherMonitorList);
    }

    // 修改天气实时检测数据
    @PutMapping("/{id}")
    public Result updateWeatherInfo(@PathVariable Long id, @RequestBody WeatherMonitor weatherMonitor) {
        weatherMonitor.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = weatherMonitorService.updateById(weatherMonitor);
        if (isSuccess) {
            return Result.success(200, "天气实时检测数据更新成功", weatherMonitor);
        } else {
            return Result.fail(404, "天气实时检测数据不存在", null);
        }
    }

    // 删除天气实时检测数据
    @DeleteMapping("/{id}")
    public Result deleteWeatherInfo(@PathVariable Long id) {
        boolean isSuccess = weatherMonitorService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "天气实时检测数据删除成功", null);
        } else {
            return Result.fail(404, "天气实时检测数据不存在", null);
        }
    }
}
