package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.port.UserPort;
import com.cinema.domain.user.User;
import com.cinema.domain.user.UserRole;
import com.cinema.infrastructure.persistence.postgres.entity.UserEntity;
import com.cinema.infrastructure.persistence.postgres.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserPersistenceAdapter implements UserPort {
    private final UserJpaRepository repo;

    public UserPersistenceAdapter(UserJpaRepository repo) { this.repo = repo; }

    @Override
    public Optional<User> findByEmail(String email) { return repo.findByEmail(email).map(this::toDomain); }

    @Override
    public Optional<User> findById(UUID id) { return repo.findById(id).map(this::toDomain); }

    @Override
    public User save(User user) {
        repo.save(toEntity(user));
        return user;
    }

    private User toDomain(UserEntity e) {
        return new User(e.getId(), e.getEmail(), e.getPasswordHash(), UserRole.valueOf(e.getRole()), e.getCreatedAt(), e.isActive());
    }

    private UserEntity toEntity(User d) {
        UserEntity e = new UserEntity();
        e.setId(d.getId()); e.setEmail(d.getEmail()); e.setPasswordHash(d.getPasswordHash()); e.setRole(d.getRole().name()); e.setCreatedAt(d.getCreatedAt()); e.setActive(d.isActive());
        return e;
    }
}
