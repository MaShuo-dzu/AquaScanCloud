package org.qinian.filter;

import lombok.RequiredArgsConstructor;
import org.qinian.exception.UnauthorizedException;
import org.qinian.utils.CollUtils;
import org.qinian.utils.JwtTool;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    public final String prefixPath = "";

    public static final List<String> excludePaths = Arrays.asList(
            "/code/*",
            "/email/*",
            "/fishermen/login",
            "/fishermen/register",
            "/fishermen/select",
            "/admin/login",
            "/admin/register",
            "/fishing-platoon/login",
            "/fishing-platoon/register",
            "/fishing-platoon/select",
            "/fishing-platoon/select-by-ik"
    );

    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取 Request
        ServerHttpRequest request = exchange.getRequest();
        // 2. 判断是否不需要拦截
        if (isExclude(request.getPath().toString())) {
            //  ⽆需拦截，直接放⾏
            return chain.filter(exchange);
        }
        // 3. 获取请求头中的 token
        String token = null;
        List<String> headers = request.getHeaders().get("token");
        if (!CollUtils.isEmpty(headers)) {
            token = headers.get(0);
        }
        // 4. 校验并解析 token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            // 如果⽆效，拦截
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }

        // 5. 如果有效，传递⽤⼾信息
        String userInfo = userId.toString();
        ServerWebExchange ex = exchange.mutate()
                .request(b -> b.header("user-info", userInfo)).build();

        // 6. 放⾏
        return chain.filter(ex);
    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : excludePaths) {
            if (antPathMatcher.match(prefixPath + pathPattern, antPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
