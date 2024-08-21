package org.qinian.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QCCInfo {
    /**
     * {
     * "Status": "112",
     * "Message": "【无效请求】您的账号剩余使用量已不足或已过期",
     * "OrderNumber": "ECICERTIFICATION2024080709174477213715",
     * "Paging": null,
     * "Result": null
     * }
     */
    private String Status;
    private String Message;
    private String OrderNumber;
    private String Paging;
    private String Result;
}
