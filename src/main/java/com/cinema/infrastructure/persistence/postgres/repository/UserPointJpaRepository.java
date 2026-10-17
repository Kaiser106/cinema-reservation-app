package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, UUID> {}
