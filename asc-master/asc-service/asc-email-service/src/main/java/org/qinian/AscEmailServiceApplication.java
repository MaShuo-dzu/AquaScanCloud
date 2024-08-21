package org.qinian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = {"org.qinian.dao"})
public class AscEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AscEmailServiceApplication.class, args);
    }
}
