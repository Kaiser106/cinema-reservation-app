package com.cinema.application.reservation;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.domain.repository.OutboxRepository;
import com.cinema.domain.repository.ReservationRepository;
import com.cinema.application.reservation.commands.ConfirmReservationCommand;
import com.cinema.domain.reservation.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfirmReservationCommandHandler implements CommandHandler<ConfirmReservationCommand, Void> {
    private final ReservationRepository reservationRepository;
    private final OutboxRepository outboxRepository;

    public ConfirmReservationCommandHandler(ReservationRepository reservationRepository, OutboxRepository outboxRepository) {
        this.reservationRepository = reservationRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    @Transactional
    public Void handle(ConfirmReservationCommand command) {
        Reservation reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.RESERVATION_NOT_FOUND, "error.reservation.notFound"));
        if (!reservation.getUserId().equals(command.userId())) {
            throw new BusinessException(ErrorCodes.RESERVATION_ACCESS_DENIED, "error.reservation.accessDenied");
        }
        reservation.confirm();
        reservationRepository.save(reservation);
        outboxRepository.storeAll(reservation.pullDomainEvents());
        return null;
    }
}
