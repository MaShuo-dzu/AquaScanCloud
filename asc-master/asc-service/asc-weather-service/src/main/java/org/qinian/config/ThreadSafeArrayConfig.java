package org.qinian.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.qinian.domain.vo.GetRaftVo;
import org.qinian.model.RemoteRaftService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

@Configuration
public class ThreadSafeArrayConfig {
    @Bean
    public Vector<WeatherLoopAddDto> threadSafeVector(RemoteRaftService client) {
        // 0.
        ObjectMapper objectMapper = new ObjectMapper();
        // 自定义的日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 使用自定义的格式创建 LocalDateTime 的反序列化器
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        objectMapper.registerModule(module);

        Vector<WeatherLoopAddDto> weatherLoopAddDtos = new Vector<>();
        List<GetRaftVo> raftList = new ArrayList<>();

        // 1.获取所有渔排点
        List<LinkedHashMap<String, Object>> dataList = (ArrayList) client.getAllFishRafts().getData();

        for (LinkedHashMap<String, Object> map : dataList) {
            // 将 LinkedHashMap 转换为 GetRaftVo
            GetRaftVo raftVo = objectMapper.convertValue(map, GetRaftVo.class);
            raftList.add(raftVo);
        }

        for (GetRaftVo getRaftVo : raftList) {
            weatherLoopAddDtos.add(new WeatherLoopAddDto(
                    getRaftVo.getRaftId(),
                    getRaftVo.getLocationId(),
                    getRaftVo.getLatitude(),
                    getRaftVo.getLongitude()
            ));
        }

        System.out.println("weatherLoopAddDtos = " + weatherLoopAddDtos);
        return weatherLoopAddDtos;
    }
}
