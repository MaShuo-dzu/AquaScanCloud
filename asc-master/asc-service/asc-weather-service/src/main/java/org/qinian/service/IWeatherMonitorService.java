package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.pojo.WeatherMonitor;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IWeatherMonitorService extends IService<WeatherMonitor> {

    void alert(WeatherMonitor weatherMonitor);
}
