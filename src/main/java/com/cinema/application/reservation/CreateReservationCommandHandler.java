package com.cinema.application.reservation;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.port.*;
import com.cinema.application.reservation.commands.CreateReservationCommand;
import com.cinema.domain.common.Money;
import com.cinema.domain.reservation.Reservation;
import com.cinema.domain.reservation.ReservationSeat;
import com.cinema.domain.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
public class CreateReservationCommandHandler implements CommandHandler<CreateReservationCommand, UUID> {
    private final SeatAvailabilityPort seatAvailabilityPort;
    private final SessionPort sessionPort;
    private final ReservationPort reservationPort;
    private final OutboxPort outboxPort;

    public CreateReservationCommandHandler(SeatAvailabilityPort seatAvailabilityPort, SessionPort sessionPort,
                                           ReservationPort reservationPort, OutboxPort outboxPort) {
        this.seatAvailabilityPort = seatAvailabilityPort;
        this.sessionPort = sessionPort;
        this.reservationPort = reservationPort;
        this.outboxPort = outboxPort;
    }

    @Override
    @Transactional
    public UUID handle(CreateReservationCommand command) {
        Session session = sessionPort.findById(command.sessionId())
                .orElseThrow(() -> new BusinessException("SESSION_NOT_FOUND", "error.session.notFound"));

        // PRIMARY concurrency strategy: DB-level pessimistic locks.
        // Locking session + seat rows in transaction prevents double-booking across app instances.
        seatAvailabilityPort.lockSessionAndSeats(command.sessionId(), command.seatIds());

        if (seatAvailabilityPort.hasReservedSeat(command.sessionId(), command.seatIds())) {
            throw new BusinessException(ErrorCodes.RESERVATION_SEAT_ALREADY_RESERVED, "error.reservation.seatAlreadyReserved");
        }

        Reservation reservation = new Reservation(UUID.randomUUID(), command.userId(), command.sessionId(), Instant.now(), Instant.now().plusSeconds(15 * 60));
        for (Long seatId : command.seatIds()) {
            reservation.addSeat(new ReservationSeat(UUID.randomUUID(), seatId, command.sessionId(), session.getPrice()));
        }

        Reservation saved = reservationPort.save(reservation);

        // Transactional outbox write happens in SAME transaction as reservation write.
        outboxPort.storeAll(saved.pullDomainEvents());
        return saved.id();
    }
}
