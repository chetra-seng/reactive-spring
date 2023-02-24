package com.chetra.reactivespring.service;

import com.chetra.reactivespring.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDto> get50Customers();
    Mono<CustomerDto> getCustomerById(Integer id);
    Mono<CustomerDto> addCustomer(Mono<CustomerDto> customerDto);
}
