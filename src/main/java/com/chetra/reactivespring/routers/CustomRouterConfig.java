package com.chetra.reactivespring.routers;

import com.chetra.reactivespring.dto.CustomerDto;
import com.chetra.reactivespring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class CustomRouterConfig {
    private final CustomerService customerService;

    @Bean
    public RouterFunction<?> customerRoute() {
        return route(GET("/customer"), request ->
            ok().body(customerService.get50Customers(), CustomerDto.class))
                .andRoute(GET("/customer/{id}"), request ->
                        ok().body(
                                customerService.getCustomerById(Integer.valueOf(request.pathVariable("id"))),
                                CustomerDto.class
                        )
                )
                .andRoute(POST("/customer"), request -> {
                    Mono<CustomerDto> dtoMono = request.bodyToMono(CustomerDto.class);
                    return created(
                            URI.create("/customer/" + dtoMono.map(CustomerDto::getId)))
                            .body(dtoMono, CustomerDto.class);
                });
    }
}
