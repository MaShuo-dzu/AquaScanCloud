package org.qinian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.qinian.dao.WeatherMonitorMapper;
import org.qinian.domain.dto.alert.AddAlertInfoDto;
import org.qinian.domain.pojo.WeatherMonitor;
import org.qinian.enums.AlertLevels;
import org.qinian.enums.AlertTypes;
import org.qinian.model.RemoteAlertService;
import org.qinian.service.IWeatherMonitorService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Service
@RequiredArgsConstructor
public class WeatherMonitorServiceImpl extends ServiceImpl<WeatherMonitorMapper, WeatherMonitor> implements IWeatherMonitorService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final RemoteAlertService remoteAlertService;

    @Override
    public void alert(WeatherMonitor weatherMonitor) {
        String key = "alert_weather_" + weatherMonitor.getFishRaftId();

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 1. 获取该渔排点的历史记录
            WeatherMonitor lastInfo = (WeatherMonitor) redisTemplate.opsForValue().get(key);

            // 2.天气数据变更警告 （这里仅以温度为例）
            BigDecimal lowThreshold = new BigDecimal("-5"); // 低温阈值
            BigDecimal highThreshold = new BigDecimal("35"); // 高温阈值

            assert lastInfo != null;
            if (lastInfo.getTemperature().compareTo(lowThreshold) < 0) {
                // 触发极寒天气警告
                remoteAlertService.createAlertInfo(
                        new AddAlertInfoDto(
                                weatherMonitor.getFishRaftId(),
                                AlertTypes.WEATHER_ALERT,
                                AlertLevels.HEAT,
                                "触发极寒天气警告"
                        )
                );
            } else if (lastInfo.getTemperature().compareTo(highThreshold) > 0) {
                // 触发极热天气警告
                remoteAlertService.createAlertInfo(
                        new AddAlertInfoDto(
                                weatherMonitor.getFishRaftId(),
                                AlertTypes.WEATHER_ALERT,
                                AlertLevels.HEAT,
                                "触发极热天气警告"
                        )
                );
            }
        } else {
            // 无该渔排点的历史记录
            redisTemplate.opsForValue().set(key, weatherMonitor);
        }
    }
}
