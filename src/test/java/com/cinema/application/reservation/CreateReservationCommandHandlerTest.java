package com.cinema.application.reservation;

import com.cinema.application.common.BusinessException;
import com.cinema.domain.repository.OutboxRepository;
import com.cinema.domain.repository.ReservationRepository;
import com.cinema.domain.repository.SeatAvailabilityRepository;
import com.cinema.application.port.SessionReadRepository;
import com.cinema.application.reservation.commands.CreateReservationCommand;
import com.cinema.domain.common.Money;
import com.cinema.domain.reservation.Reservation;
import com.cinema.domain.session.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReservationCommandHandlerTest {
    @Mock SeatAvailabilityRepository seatAvailabilityRepository;
    @Mock SessionReadRepository sessionReadRepository;
    @Mock ReservationRepository reservationRepository;
    @Mock OutboxRepository outboxRepository;
    @InjectMocks CreateReservationCommandHandler handler;

    @Test
    void shouldCreateReservationWhenSeatsAvailable() {
        UUID sessionId = UUID.randomUUID();
        when(sessionReadRepository.findById(sessionId)).thenReturn(Optional.of(new Session(sessionId, UUID.randomUUID(), 1L, Instant.now(), Instant.now().plusSeconds(3600), Money.of(new BigDecimal("80")))));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(seatAvailabilityRepository.hasReservedSeat(eq(sessionId), any())).thenReturn(false);

        UUID id = handler.handle(new CreateReservationCommand(UUID.randomUUID(), sessionId, List.of(1L, 2L)));
        assertNotNull(id);

        verify(seatAvailabilityRepository).lockSessionAndSeats(eq(sessionId), eq(List.of(1L,2L)));
        verify(outboxRepository).storeAll(any());
    }

    @Test
    void shouldFailWhenSeatAlreadyReserved() {
        UUID sessionId = UUID.randomUUID();
        when(sessionReadRepository.findById(sessionId)).thenReturn(Optional.of(new Session(sessionId, UUID.randomUUID(), 1L, Instant.now(), Instant.now().plusSeconds(3600), Money.of(new BigDecimal("80")))));
        when(seatAvailabilityRepository.hasReservedSeat(eq(sessionId), any())).thenReturn(true);

        assertThrows(BusinessException.class, () -> handler.handle(new CreateReservationCommand(UUID.randomUUID(), sessionId, List.of(1L))));
    }
}
