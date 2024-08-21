package org.qinian.domain.dto.fishingPlatoon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FishingPlatoonRegisterFormDTO {

    private String username;

    private String password;

    private String email;

    private String phone;

    private String province;

    private String city;

    private String town;

    private String blImageBase;  // 营业许可的图像编码
}
