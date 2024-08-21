package org.qinian.domain.dto.raftMonitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRaftMonitor {
    private Long fishRaftId;

    private Integer raftNumber;

    private Double raftArea;
}
