package com.chetra.reactivespring.service;

import com.chetra.reactivespring.dao.CustomerDao;
import com.chetra.reactivespring.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDao customerDao;

    @Override
    public Flux<CustomerDto> get50Customers() {
        return customerDao.get50Customers();
    }

    @Override
    public Mono<CustomerDto> getCustomerById(Integer id) {
        return customerDao.getCustomerById(id);
    }

    @Override
    public Mono<CustomerDto> addCustomer(Mono<CustomerDto> customerDto) {
        return null;
    }
}
