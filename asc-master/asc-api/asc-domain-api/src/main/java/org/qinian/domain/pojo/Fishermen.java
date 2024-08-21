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
@TableName("fishermen")
public class Fishermen implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Long Id;

    private Long fishingPlatoonId;

    private String username;

    private String password;

    private String Email;

    private String Phone;

    private UserStatus State;

    private LocalDateTime createTime;

    private LocalDateTime loginTime;

    private LocalDateTime updateTime;


}
