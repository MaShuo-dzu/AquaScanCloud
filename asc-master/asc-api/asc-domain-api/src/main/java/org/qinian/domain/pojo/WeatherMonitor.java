package org.qinian.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("weather_monitor")
public class WeatherMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Long Id;

    private Long fishRaftId;

    private Long locationId;

    private String province;

    private String city;

    private String adcode;

    private String weather;

    private BigDecimal temperature;

    private String windDirection;

    private String windPower;

    private Integer humidity;

    private LocalDateTime reportTime;

    private LocalDateTime createTime;


}
