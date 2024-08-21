package org.qinian.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "baidu")
public class BaiduProperties {
    private String ak;
    private String sk;
}
