package org.qinian.model;

import org.qinian.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用business license检测（ocr）服务
 * Created by Qinian on 2020/5/18.
 */
@FeignClient("algorithm-service")
public interface RemoteBLOCRService {

    @PostMapping("/baidu/getBusinessLic")
    Result getBusinessLic(@RequestParam("imageBase") String imageBase);
}
