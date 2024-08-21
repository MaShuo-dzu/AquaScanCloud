package org.qinian.service.impl;

import lombok.RequiredArgsConstructor;
import org.qinian.entity.BusinessLicInfo;
import org.qinian.service.BaiduOCRService;
import org.qinian.utils.BaiDuOCRUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class BaiduOCRServiceImpl implements BaiduOCRService {
    private final BaiDuOCRUtil baiDuOCRUtil;

    @Override
    public BusinessLicInfo getBusinessLicHashMapByImageBase(String imageBase) {
        imageBase = imageBase.replaceAll("\r\n", "");
        imageBase = imageBase.replaceAll("\\+", "%2B");

        String httpUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";  // 营业执照

        String httpArg = "image=" + imageBase;
        String request = baiDuOCRUtil.request(httpUrl, httpArg);

        if (!baiDuOCRUtil.checkAvailable(request)) {
            // false error_code
            return null;
        }

        HashMap<String, String> busniessLicHashMap = baiDuOCRUtil.getBusinessLicHashMap(request);

        BusinessLicInfo businessLicInfo = new BusinessLicInfo();
        businessLicInfo.setAddress(busniessLicHashMap.get("地址"));
        businessLicInfo.setCeo(busniessLicHashMap.get("法人"));
        businessLicInfo.setCode(busniessLicHashMap.get("证件编号"));
        businessLicInfo.setCompanyName(busniessLicHashMap.get("单位名称"));
        businessLicInfo.setCreditId(busniessLicHashMap.get("社会信用代码"));
        businessLicInfo.setDate(busniessLicHashMap.get("有效期"));
        businessLicInfo.setType(busniessLicHashMap.get("类型"));

        return businessLicInfo;
    }

    @Override
    public HashMap<String, String> getIdCardHashMapByImageBase(String imageBase) {
        return null;
    }
}
