package com.cinema.domain.customer.repository;

import com.cinema.domain.customer.model.Customer;

public interface CustomerRepository {
    Customer save(Customer customer);
}
