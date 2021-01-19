package com.atguigu.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/*逻辑：实现接口，容器注入，获取http中想要的信息，进行判断，根据返回结果进行下一把操作*/

@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {

    //chain：过滤链   StatusCode：状态码
    // ServerWebExchange:服务网络交换器，用来存放request，response属性，存放请求实例，响应实例
    //getQueryParams:获取映射
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("******come in MyLogGateWayFilter:  "+new Date());

        String uname = exchange.getRequest().getQueryParams().getFirst("name");

        if (uname == null){
            log.info("********用户名为null，非法用户");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {  //加载过滤器的顺序
        return 0;
    }
}
