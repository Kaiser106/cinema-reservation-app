package com.cinema.application.port;

import com.cinema.domain.customer.Customer;

public interface CustomerPort {
    Customer save(Customer customer);
}
