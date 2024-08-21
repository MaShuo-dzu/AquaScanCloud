package org.qinian.domain.dto.email.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCodeDto {
    private String mail;

    private Integer length;
}
