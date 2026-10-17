package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.application.movie.dto.MovieDto;
import com.cinema.infrastructure.persistence.postgres.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, UUID> {
    @Query("select new com.cinema.application.movie.dto.MovieDto(m.id,m.title,m.durationMinutes,m.genre,m.rating,m.posterUrl) from MovieEntity m")
    Page<MovieDto> findMovieDtos(Pageable pageable);

    @Query("select new com.cinema.application.movie.dto.MovieDto(m.id,m.title,m.durationMinutes,m.genre,m.rating,m.posterUrl) from MovieEntity m where m.id=:id")
    Optional<MovieDto> findMovieDtoById(UUID id);
}
