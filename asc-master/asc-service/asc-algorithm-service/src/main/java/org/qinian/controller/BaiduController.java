package org.qinian.controller;

import org.qinian.domain.Result;
import org.qinian.entity.BusinessLicInfo;
import org.qinian.service.BaiduOCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 百度控制器
 * </p>
 *
 * @author qinian
 * @since 2024-08-05
 */
@RestController
@RequestMapping("/baidu")
public class BaiduController {
    @Autowired
    private BaiduOCRService baiduOCRService;

    @PostMapping("/getBusinessLic")
    public Result getBusinessLic(
            @RequestBody String imageBase
    ) {
        // 调用百度OCR服务
        BusinessLicInfo businessLicInfo = baiduOCRService.getBusinessLicHashMapByImageBase(imageBase);
        return Result.success(200, "获取成功", businessLicInfo);
    }
}
