package com.chetra.reactivespring.dao;

import com.chetra.reactivespring.dto.CustomerDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
public class CustomerDao {
    public Flux<CustomerDto> get50Customers() {
        return Flux
                .range(1, 50)
                .flatMap(i -> Mono
                        .just(i)
                        .map(j ->
                                new CustomerDto(j, "Customer" + j)
                        )
                ).subscribeOn(Schedulers.parallel());
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        if (id >=1 && id <=50) {
           return Mono.just(new CustomerDto(id, "Customer" + id));
        }

        return Mono.empty();
    }

    public Mono<CustomerDto> postCustomer(Mono<CustomerDto> customerDto) {
        return customerDto;
    }
}
