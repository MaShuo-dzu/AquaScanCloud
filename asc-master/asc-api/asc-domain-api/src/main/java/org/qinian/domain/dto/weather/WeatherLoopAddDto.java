package org.qinian.domain.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherLoopAddDto {
    private Long fishRaftId;

    private Long locationId;

    private Double latitude;

    private Double longitude;
}
