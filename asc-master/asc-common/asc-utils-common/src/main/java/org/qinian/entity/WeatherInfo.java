package org.qinian.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherInfo {
    private String status;
    private String count;
    private String info;
    private String infocode;
    private Live[] lives;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Live {
        @JsonProperty("province")
        private String province;

        @JsonProperty("city")
        private String city;

        @JsonProperty("adcode")
        private String adcode;

        @JsonProperty("weather")
        private String weather;

        @JsonProperty("temperature")
        private BigDecimal temperature;

        @JsonProperty("winddirection")
        private String windDirection;

        @JsonProperty("windpower")
        private String windPower;

        @JsonProperty("humidity")
        private Integer humidity;

        @JsonProperty("reporttime")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date reportTime;
    }
}
