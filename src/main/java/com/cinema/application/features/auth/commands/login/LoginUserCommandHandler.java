package com.cinema.application.features.auth.commands.login;

import com.cinema.application.cqrs.CommandHandler;
import com.cinema.application.features.auth.ports.PasswordHasherPort;
import com.cinema.application.features.auth.ports.SessionAuthPort;
import com.cinema.application.shared.exception.BusinessException;
import com.cinema.application.shared.exception.ErrorCodes;
import com.cinema.domain.user.model.User;
import com.cinema.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class LoginUserCommandHandler implements CommandHandler<LoginUserCommand, String> {
    private final UserRepository userRepository;
    private final SessionAuthPort sessionAuthPort;
    private final PasswordHasherPort passwordEncoderService;

    public LoginUserCommandHandler(UserRepository userRepository, SessionAuthPort sessionAuthPort, PasswordHasherPort passwordEncoderService) {
        this.userRepository = userRepository;
        this.sessionAuthPort = sessionAuthPort;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    @Transactional
    public String handle(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new BusinessException(ErrorCodes.AUTH_INVALID_CREDENTIALS, "error.auth.invalidCredentials"));

        if (!passwordEncoderService.matches(command.password(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCodes.AUTH_INVALID_CREDENTIALS, "error.auth.invalidCredentials");
        }

        // Session fixation prevention: always create a brand new secure token on login.
        return sessionAuthPort.createSession(user.getId(), user.getRole().name(), command.userAgent(), command.ipAddress(), Instant.now().plusSeconds(60 * 60 * 12));
    }
}
