package com.cinema.application.features.reservation.commands.create;

import com.cinema.application.cqrs.CommandHandler;
import com.cinema.application.features.session.ports.SessionReadRepository;
import com.cinema.application.shared.exception.BusinessException;
import com.cinema.application.shared.exception.ErrorCodes;
import com.cinema.application.shared.ports.OutboxRepository;
import com.cinema.domain.reservation.model.Reservation;
import com.cinema.domain.reservation.model.ReservationSeat;
import com.cinema.domain.reservation.repository.ReservationRepository;
import com.cinema.domain.reservation.repository.SeatAvailabilityRepository;
import com.cinema.domain.session.model.Session;
import com.cinema.domain.shared.vobj.Money;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
public class CreateReservationCommandHandler implements CommandHandler<CreateReservationCommand, UUID> {
    private final SeatAvailabilityRepository seatAvailabilityRepository;
    private final SessionReadRepository sessionReadRepository;
    private final ReservationRepository reservationRepository;
    private final OutboxRepository outboxRepository;

    public CreateReservationCommandHandler(SeatAvailabilityRepository seatAvailabilityRepository, SessionReadRepository sessionReadRepository,
                                           ReservationRepository reservationRepository, OutboxRepository outboxRepository) {
        this.seatAvailabilityRepository = seatAvailabilityRepository;
        this.sessionReadRepository = sessionReadRepository;
        this.reservationRepository = reservationRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    @Transactional
    public UUID handle(CreateReservationCommand command) {
        Session session = sessionReadRepository.findById(command.sessionId())
                .orElseThrow(() -> new BusinessException("SESSION_NOT_FOUND", "error.session.notFound"));

        // PRIMARY concurrency strategy: DB-level pessimistic locks.
        // Locking session + seat rows in transaction prevents double-booking across app instances.
        seatAvailabilityRepository.lockSessionAndSeats(command.sessionId(), command.seatIds());

        if (seatAvailabilityRepository.hasReservedSeat(command.sessionId(), command.seatIds())) {
            throw new BusinessException(ErrorCodes.RESERVATION_SEAT_ALREADY_RESERVED, "error.reservation.seatAlreadyReserved");
        }

        Reservation reservation = new Reservation(UUID.randomUUID(), command.userId(), command.sessionId(), Instant.now(), Instant.now().plusSeconds(15 * 60));
        for (Long seatId : command.seatIds()) {
            reservation.addSeat(new ReservationSeat(UUID.randomUUID(), seatId, command.sessionId(), session.getPrice()));
        }

        Reservation saved = reservationRepository.save(reservation);

        // Transactional outbox write happens in SAME transaction as reservation write.
        outboxRepository.storeAll(saved.pullDomainEvents());
        return saved.id();
    }
}
