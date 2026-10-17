package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.port.CustomerPort;
import com.cinema.domain.customer.Customer;
import com.cinema.infrastructure.persistence.postgres.entity.CustomerEntity;
import com.cinema.infrastructure.persistence.postgres.repository.CustomerJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceAdapter implements CustomerPort {
    private final CustomerJpaRepository repo;

    public CustomerPersistenceAdapter(CustomerJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity e = new CustomerEntity();
        e.setId(customer.getId());
        e.setUserId(customer.getUserId());
        e.setFullName(customer.getFullName());
        e.setPhone(customer.getPhone());
        e.setCreatedAt(customer.getCreatedAt());
        repo.save(e);
        return customer;
    }
}
