package org.qinian.service;

import org.qinian.entity.BusinessLicInfo;

import java.util.HashMap;

public interface BaiduOCRService {
    /**
     * 识别营业执照
     *
     * @param imageBase
     * @return HashMap<String, String> or null
     */
    BusinessLicInfo getBusinessLicHashMapByImageBase(String imageBase);

    HashMap<String, String> getIdCardHashMapByImageBase(String imageBase);
}
