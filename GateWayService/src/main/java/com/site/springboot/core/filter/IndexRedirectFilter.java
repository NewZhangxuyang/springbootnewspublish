package com.site.springboot.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class IndexRedirectFilter extends AbstractGatewayFilterFactory<IndexRedirectFilter.Config> {


    private final Logger logger = LoggerFactory.getLogger(RedirectFilter.class);

    public IndexRedirectFilter() {
        super(Config.class);
    }

    @SuppressWarnings("unused")
    private boolean isAuthorizationValid(String authorizationHeader) {
        return true;
    }

    @SuppressWarnings("unused")
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.defer(() -> {
            if (!exchange.getResponse().isCommitted()) {
                if (Objects.equals(exchange.getResponse().getStatusCode(), HttpStatus.OK)) {
                    String url = exchange.getRequest().getURI().toString();
                    String urls = "http://localhost:10010";
                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    try {
                        String s = urls + "/admin";
                        exchange.getResponse().getHeaders().set("location", urls + "/admin");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return exchange.getResponse().setComplete();
                }
            }
            return Mono.empty();
        }));
    }

    public static class Config {
    }

}
