package org.qinian.model;

import org.qinian.domain.Result;
import org.qinian.domain.dto.alert.AddAlertInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("alert-service")
public interface RemoteAlertService {

    @PostMapping("/alert-info")
    Result createAlertInfo(@RequestBody AddAlertInfoDto addAlertInfoDto);
}
