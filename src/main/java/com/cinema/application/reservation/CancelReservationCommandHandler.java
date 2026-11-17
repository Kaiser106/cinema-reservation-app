package com.cinema.application.reservation;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.domain.repository.OutboxRepository;
import com.cinema.domain.repository.ReservationRepository;
import com.cinema.application.reservation.commands.CancelReservationCommand;
import com.cinema.domain.reservation.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CancelReservationCommandHandler implements CommandHandler<CancelReservationCommand, Void> {
    private final ReservationRepository reservationRepository;
    private final OutboxRepository outboxRepository;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository, OutboxRepository outboxRepository) {
        this.reservationRepository = reservationRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    @Transactional
    public Void handle(CancelReservationCommand command) {
        Reservation reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.RESERVATION_NOT_FOUND, "error.reservation.notFound"));
        if (!reservation.getUserId().equals(command.userId())) {
            throw new BusinessException(ErrorCodes.RESERVATION_ACCESS_DENIED, "error.reservation.accessDenied");
        }
        reservation.cancel(command.reason());
        reservationRepository.save(reservation);
        outboxRepository.storeAll(reservation.pullDomainEvents());
        return null;
    }
}
