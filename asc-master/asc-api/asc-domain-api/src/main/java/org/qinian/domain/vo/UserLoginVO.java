package org.qinian.domain.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private String token;
    private Long userId;
    private String username;
}
