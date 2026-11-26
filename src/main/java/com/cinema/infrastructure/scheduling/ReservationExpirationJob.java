package com.cinema.infrastructure.scheduling;

import com.cinema.application.shared.ports.OutboxRepository;
import com.cinema.domain.reservation.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class ReservationExpirationJob {
    private final ReservationRepository reservationRepository;
    private final OutboxRepository outboxRepository;

    public ReservationExpirationJob(ReservationRepository reservationRepository, OutboxRepository outboxRepository) {
        this.reservationRepository = reservationRepository;
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelayString = "${app.reservation-expire.fixed-delay-ms:30000}")
    @Transactional
    public void expirePendingReservations() {
        reservationRepository.findPendingExpired(Instant.now(), 100).forEach(r -> {
            r.cancel("expired");
            reservationRepository.save(r);
            outboxRepository.storeAll(r.pullDomainEvents());
        });
    }
}
