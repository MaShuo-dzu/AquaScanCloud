package org.qinian.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessLicInfo {
    private String address;
    private String ceo;
    private String code;
    private String companyName;
    private String creditId;
    private String date;
    private String type;
}
