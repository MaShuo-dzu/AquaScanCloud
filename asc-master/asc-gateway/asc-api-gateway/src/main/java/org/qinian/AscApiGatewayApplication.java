package org.qinian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class AscApiGatewayApplication {

    /**
     * 启动 API 网关 服务
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AscApiGatewayApplication.class, args);
    }
}
