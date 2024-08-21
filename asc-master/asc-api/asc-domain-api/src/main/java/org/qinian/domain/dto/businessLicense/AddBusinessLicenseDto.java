package org.qinian.domain.dto.businessLicense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBusinessLicenseDto {
    private String businessLicense;

    private String licenseNumber;

    private LocalDate licenseCreateTime;

    private LocalDate licenseEffectiveTime;
}
