package org.qinian.domain.dto.identityCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddIdentityCardDto {
    private Integer userType;

    private Long userId;

    private String name;

    private String idCardNumber;

    private Integer sex;

    private LocalDate birthDate;

    private String home;
}
