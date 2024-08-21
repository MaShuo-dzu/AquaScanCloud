package org.qinian.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AlertTypes {
    AI_ALERT(1L, "算法警告"),
    WEATHER_ALERT(2L, "天气警告"),
    ;
    @EnumValue
    final
    Long value;
    final String desc;

    AlertTypes(Long value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
