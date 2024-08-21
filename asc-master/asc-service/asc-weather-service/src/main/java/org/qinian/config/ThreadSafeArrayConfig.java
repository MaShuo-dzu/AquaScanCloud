package org.qinian.config;

import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.qinian.domain.pojo.FishRaftPoint;
import org.qinian.domain.pojo.Location;
import org.qinian.domain.vo.GetRaftVo;
import org.qinian.model.RemoteLocationService;
import org.qinian.model.RemoteRaftService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Vector;

@Configuration
public class ThreadSafeArrayConfig {

    @Bean
    public Vector<WeatherLoopAddDto> threadSafeVector(RemoteRaftService client, RemoteLocationService remoteLocationService) {
        Vector<WeatherLoopAddDto> weatherLoopAddDtos = new Vector<>();
        // 1.获取所有渔排点
        List<GetRaftVo> dataList = (List<GetRaftVo>) client.getAllFishRafts().getData();
        for (GetRaftVo data : dataList) {
            weatherLoopAddDtos.add(new WeatherLoopAddDto(
                    data.getRaftId(),
                    data.getLocationId(),
                    data.getLatitude(),
                    data.getLongitude()
            ));
        }

        return weatherLoopAddDtos;
    }
}
