package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.domain.repository.UserPointRepository;
import com.cinema.domain.point.UserPoint;
import com.cinema.infrastructure.persistence.postgres.entity.UserPointEntity;
import com.cinema.infrastructure.persistence.postgres.repository.UserPointJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserPointPersistenceAdapter implements UserPointRepository {
    private final UserPointJpaRepository repo;

    public UserPointPersistenceAdapter(UserPointJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<UserPoint> findByUserId(UUID userId) {
        return repo.findById(userId).map(e -> new UserPoint(e.getUserId(), e.getBalance()));
    }

    @Override
    public UserPoint save(UserPoint point) {
        UserPointEntity e = new UserPointEntity();
        e.setUserId(point.id());
        e.setBalance(point.getBalance());
        repo.save(e);
        return point;
    }
}
