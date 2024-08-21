package org.qinian.model;

import org.qinian.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("fishRaft-service")
public interface RemoteRaftService {
    @GetMapping("/fish-raft-point")
    Result getAllFishRafts();
}
