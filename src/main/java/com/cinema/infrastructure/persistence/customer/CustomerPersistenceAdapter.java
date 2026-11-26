package com.cinema.infrastructure.persistence.customer;

import com.cinema.domain.customer.model.Customer;
import com.cinema.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceAdapter implements CustomerRepository {
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
