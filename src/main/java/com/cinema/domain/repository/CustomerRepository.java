package com.cinema.domain.repository;

import com.cinema.domain.customer.Customer;

public interface CustomerRepository {
    Customer save(Customer customer);
}
