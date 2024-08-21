package org.qinian.domain.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendVerificationCodeDto {
    /**
     * String to, String subject, String code
     */

    private String to;
    private String subject;
    private String code;
}
