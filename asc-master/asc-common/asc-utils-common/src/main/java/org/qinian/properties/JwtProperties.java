package org.qinian.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
public class JwtProperties {
    @Value("classpath:asc.jks")
    private Resource location;

    private String password = "asc123";

    private String alias = "asc";

    private Duration tokenTTL = Duration.ofMinutes(3000);
}
