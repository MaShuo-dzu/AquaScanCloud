package org.qinian.domain.dto.fishermen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FishermenRegisterFormDTO {
    private String username;

    private String password;

    private String email;

    private String phone;

    private String invitationCode;  // 唯一邀请码
}
