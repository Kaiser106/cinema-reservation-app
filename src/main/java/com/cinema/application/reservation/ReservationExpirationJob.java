package com.cinema.application.reservation;

import com.cinema.application.port.OutboxPort;
import com.cinema.application.port.ReservationPort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class ReservationExpirationJob {
    private final ReservationPort reservationPort;
    private final OutboxPort outboxPort;

    public ReservationExpirationJob(ReservationPort reservationPort, OutboxPort outboxPort) {
        this.reservationPort = reservationPort;
        this.outboxPort = outboxPort;
    }

    @Scheduled(fixedDelayString = "${app.reservation-expire.fixed-delay-ms:30000}")
    @Transactional
    public void expirePendingReservations() {
        reservationPort.findPendingExpired(Instant.now(), 100).forEach(r -> {
            r.cancel("expired");
            reservationPort.save(r);
            outboxPort.storeAll(r.pullDomainEvents());
        });
    }
}
