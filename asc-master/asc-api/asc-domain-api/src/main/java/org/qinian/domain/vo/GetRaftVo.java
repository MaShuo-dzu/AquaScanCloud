package org.qinian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRaftVo {
    private String id;

    private Long parentId;

    private Long raftId;

    private Long locationId;

    private String name;

    private Double longitude;  // 经度

    private Double latitude;  // 纬度

    private Integer zoom;

    private BigDecimal size;

    private String message;

    private String mask;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
