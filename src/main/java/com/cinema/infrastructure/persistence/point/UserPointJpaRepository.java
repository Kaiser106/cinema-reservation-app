package com.cinema.infrastructure.persistence.point;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, UUID> {}
