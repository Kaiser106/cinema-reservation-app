package com.cinema.application.auth;

import com.cinema.application.auth.commands.LoginUserCommand;
import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.port.SessionAuthPort;
import com.cinema.application.port.UserPort;
import com.cinema.domain.user.User;
import com.cinema.infrastructure.auth.PasswordEncoderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class LoginUserCommandHandler implements CommandHandler<LoginUserCommand, String> {
    private final UserPort userPort;
    private final SessionAuthPort sessionAuthPort;
    private final PasswordEncoderService passwordEncoderService;

    public LoginUserCommandHandler(UserPort userPort, SessionAuthPort sessionAuthPort, PasswordEncoderService passwordEncoderService) {
        this.userPort = userPort;
        this.sessionAuthPort = sessionAuthPort;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    @Transactional
    public String handle(LoginUserCommand command) {
        User user = userPort.findByEmail(command.email())
                .orElseThrow(() -> new BusinessException(ErrorCodes.AUTH_INVALID_CREDENTIALS, "error.auth.invalidCredentials"));

        if (!passwordEncoderService.matches(command.password(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCodes.AUTH_INVALID_CREDENTIALS, "error.auth.invalidCredentials");
        }

        // Session fixation prevention: always create a brand new secure token on login.
        return sessionAuthPort.createSession(user.getId(), user.getRole().name(), command.userAgent(), command.ipAddress(), Instant.now().plusSeconds(60 * 60 * 12));
    }
}
