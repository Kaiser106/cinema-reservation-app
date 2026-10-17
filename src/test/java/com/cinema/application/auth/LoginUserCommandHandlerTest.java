package com.cinema.application.auth;

import com.cinema.application.auth.commands.LoginUserCommand;
import com.cinema.application.common.BusinessException;
import com.cinema.application.port.SessionAuthPort;
import com.cinema.application.port.UserPort;
import com.cinema.domain.user.User;
import com.cinema.domain.user.UserRole;
import com.cinema.infrastructure.auth.PasswordEncoderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserCommandHandlerTest {
    @Mock UserPort userPort;
    @Mock SessionAuthPort sessionAuthPort;
    @Mock PasswordEncoderService passwordEncoderService;
    @InjectMocks LoginUserCommandHandler handler;

    @Test
    void shouldLoginAndCreateSession() {
        User u = new User(UUID.randomUUID(), "a@a.com", "hash", UserRole.CUSTOMER, Instant.now(), true);
        when(userPort.findByEmail("a@a.com")).thenReturn(Optional.of(u));
        when(passwordEncoderService.matches("12345678", "hash")).thenReturn(true);
        when(sessionAuthPort.createSession(any(), any(), any(), any(), any())).thenReturn("token");

        String token = handler.handle(new LoginUserCommand("a@a.com", "12345678", "agent", "127.0.0.1"));
        assertEquals("token", token);
    }

    @Test
    void shouldFailForInvalidPassword() {
        User u = new User(UUID.randomUUID(), "a@a.com", "hash", UserRole.CUSTOMER, Instant.now(), true);
        when(userPort.findByEmail("a@a.com")).thenReturn(Optional.of(u));
        when(passwordEncoderService.matches("bad", "hash")).thenReturn(false);

        assertThrows(BusinessException.class, () -> handler.handle(new LoginUserCommand("a@a.com", "bad", "agent", "127.0.0.1")));
    }
}
