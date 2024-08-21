package org.qinian.domain.dto.email.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCodeDto {
    private String email;
    private String code;
}
