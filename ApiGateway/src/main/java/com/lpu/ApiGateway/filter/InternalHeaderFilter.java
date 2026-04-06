package com.lpu.ApiGateway.filter;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
 
import reactor.core.publisher.Mono;
 
@Component
public class InternalHeaderFilter implements GlobalFilter, Ordered {
 
	private static final Logger logger = LoggerFactory.getLogger(InternalHeaderFilter.class);
    private static final String HEADER = "X-Internal-Request";
 
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.debug("Injecting internal gateway header into request: {}", exchange.getRequest().getURI().getPath());
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(HEADER, "gateway")
                .build();
 
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
 
    @Override
    public int getOrder() {
        return 1; // run early
    }
}