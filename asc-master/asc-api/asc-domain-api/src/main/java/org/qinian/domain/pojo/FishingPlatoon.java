package org.qinian.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.qinian.enums.UserStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fishing_platoon")
public class FishingPlatoon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Long Id;

    private Long businessLicenseId;

    private String username;

    private String password;

    private String email;

    private String phone;

    private UserStatus state;

    private String invitationCode;

    private LocalDateTime createTime;

    private LocalDateTime loginTime;

    private LocalDateTime updateTime;

    private String province;

    private String city;

    private String town;


}
