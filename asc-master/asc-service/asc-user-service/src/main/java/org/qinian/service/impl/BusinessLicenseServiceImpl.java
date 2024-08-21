package org.qinian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.qinian.dao.BusinessLicenseMapper;
import org.qinian.domain.Result;
import org.qinian.domain.pojo.BusinessLicense;
import org.qinian.entity.BusinessLicInfo;
import org.qinian.entity.QCCInfo;
import org.qinian.model.RemoteBLOCRService;
import org.qinian.service.IBusinessLicenseService;
import org.qinian.utils.QCCUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Service
@RequiredArgsConstructor
public class BusinessLicenseServiceImpl extends ServiceImpl<BusinessLicenseMapper, BusinessLicense> implements IBusinessLicenseService {
    private final RemoteBLOCRService remoteBLOCRService;

    @Override
    public QCCInfo checkBL(String imageBase) {
        // 1.ocr识别
        Result result = remoteBLOCRService.getBusinessLic(imageBase);

        if (result.getCode() != 200) {
            return null;
        }

        // 2.QCC检测合法性
        String request = QCCUtil.request((BusinessLicInfo) result.getData());

        if (!QCCUtil.check(request)) {
            return null;
        }

        // 3.反序列化为json对象
        JSONObject jsonObject = new JSONObject(request);

        // 从JSONObject中获取数据
        String status = jsonObject.getString("Status");
        String message = jsonObject.getString("Message");
        String orderNumber = jsonObject.getString("OrderNumber");
        String paging = jsonObject.getString("Paging");
        String resultData = jsonObject.getString("Result");

        return new QCCInfo(status, message, orderNumber, paging, resultData);
    }
}
