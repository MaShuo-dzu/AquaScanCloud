package org.qinian.domain.dto.fishermen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FishermenLoginFormDTO {
    private String username;
    private String password;
}
