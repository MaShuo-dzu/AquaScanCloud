package org.qinian.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.qinian.enums.AIStatus;

import java.io.Serializable;

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
@TableName("algorithm")
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Long Id;

    private String name;

    private String Type;

    private String level;

    private String Message;

    private String url;

    private Integer port;

    private String paramPath;

    private AIStatus state;
}
