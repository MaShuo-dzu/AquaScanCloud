package org.qinian.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AlertLevels {
    LAZY("低风险", "算法警告"),
    HEAT("高风险", "天气警告"),
    ;
    @EnumValue
    final
    String value;
    final String desc;

    AlertLevels(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
