package org.qinian.domain.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qinian.entity.WeatherInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherAddDto {
    private Long fishRaftId;

    private Long locationId;

    private WeatherInfo.Live live;
}
