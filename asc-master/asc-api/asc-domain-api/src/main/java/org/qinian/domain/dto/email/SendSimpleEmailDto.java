package org.qinian.domain.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendSimpleEmailDto {
    /**
     * String to, String subject, String text
     */

    private String to;
    private String subject;
    private String text;
}
