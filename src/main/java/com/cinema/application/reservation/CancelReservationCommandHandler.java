package com.cinema.application.reservation;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.port.OutboxPort;
import com.cinema.application.port.ReservationPort;
import com.cinema.application.reservation.commands.CancelReservationCommand;
import com.cinema.domain.reservation.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CancelReservationCommandHandler implements CommandHandler<CancelReservationCommand, Void> {
    private final ReservationPort reservationPort;
    private final OutboxPort outboxPort;

    public CancelReservationCommandHandler(ReservationPort reservationPort, OutboxPort outboxPort) {
        this.reservationPort = reservationPort;
        this.outboxPort = outboxPort;
    }

    @Override
    @Transactional
    public Void handle(CancelReservationCommand command) {
        Reservation reservation = reservationPort.findById(command.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.RESERVATION_NOT_FOUND, "error.reservation.notFound"));
        if (!reservation.getUserId().equals(command.userId())) {
            throw new BusinessException(ErrorCodes.RESERVATION_ACCESS_DENIED, "error.reservation.accessDenied");
        }
        reservation.cancel(command.reason());
        reservationPort.save(reservation);
        outboxPort.storeAll(reservation.pullDomainEvents());
        return null;
    }
}
