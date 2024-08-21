package com.qinian.baidu.service;

import org.junit.jupiter.api.Test;
import org.qinian.AscAlgorithmServiceApplication;
import org.qinian.entity.BusinessLicInfo;
import org.qinian.service.BaiduOCRService;
import org.qinian.utils.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest(classes = AscAlgorithmServiceApplication.class)
public class BaiDuTest {
    @Autowired
    private BaiduOCRService baiduOCRService;

    @Test
    public void BLTest() {
        //获取本地的绝对路径图片
        File file = new File("E:\\springProject\\AquaScanCloud\\asc-master\\asc-service\\asc-algorithm-service\\src\\main\\resources\\static\\0.jpg");
        //进行BASE64位编码
        String imageBase = Base64Util.encodeImageToBase64(file, true);

        BusinessLicInfo businessLicHashMapByImageBase = baiduOCRService.getBusinessLicHashMapByImageBase(imageBase);

        System.out.println(businessLicHashMapByImageBase);
    }
}
