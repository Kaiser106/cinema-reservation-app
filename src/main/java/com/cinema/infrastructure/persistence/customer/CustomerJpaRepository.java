package com.cinema.infrastructure.persistence.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
    java.util.Optional<CustomerEntity> findByUserId(UUID userId);
}
