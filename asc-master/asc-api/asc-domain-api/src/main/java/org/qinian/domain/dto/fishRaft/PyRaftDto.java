package org.qinian.domain.dto.fishRaft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PyRaftDto {
    private Long id;

    private Long fishermen_id;

    private Double longitude;

    private Double latitude;

    private String name;

    private Integer Zoom;

    private Double Size;

    private String message;

    private String mask;
}
