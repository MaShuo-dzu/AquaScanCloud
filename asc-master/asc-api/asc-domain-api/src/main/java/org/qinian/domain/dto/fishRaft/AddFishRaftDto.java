package org.qinian.domain.dto.fishRaft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFishRaftDto {
    private Long fishermenId;

    private AddLocationDto locationDto;

    private String name;

    private Integer Zoom;

    private Double Size;

    private String Message;

    private String Mask;
}
