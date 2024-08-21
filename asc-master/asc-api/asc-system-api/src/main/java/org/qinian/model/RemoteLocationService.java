package org.qinian.model;

import org.qinian.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("location")
public interface RemoteLocationService {
    @GetMapping("/location/{id}")
    Result getLocationById(@PathVariable Long id);
}
