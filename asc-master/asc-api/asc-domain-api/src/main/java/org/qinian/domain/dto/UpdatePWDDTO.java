package org.qinian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePWDDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
