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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Component
public class RedirectFilter extends AbstractGatewayFilterFactory<RedirectFilter.Config> {

    private final Logger logger = LoggerFactory.getLogger(RedirectFilter.class);

    public RedirectFilter() {
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
                System.out.println("status code: " + exchange.getResponse().getStatusCode());
                if (Objects.equals(exchange.getResponse().getStatusCode(), HttpStatus.OK)) {
                    String locationPath = "";
                    String url = exchange.getRequest().getURI().toString();
                    String urls = "http://localhost:28083";
                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    try {
                        locationPath = new URL(url).getPath();
                        String[] s = locationPath.split("validate");
                        locationPath = s[1];
                        exchange.getResponse().getHeaders().set("location", urls + locationPath);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return exchange.getResponse().setComplete();
                }
                System.out.println("status code: " + exchange.getResponse().getStatusCode());
                if (Objects.equals(exchange.getResponse().getStatusCode(), HttpStatus.FORBIDDEN)) {
                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    exchange.getResponse().getHeaders().set("location", "http://localhost:10010/login");
                    return exchange.getResponse().setComplete();
                }
            }

            return Mono.empty();
        }));
    }

    public static class Config {
    }
}
