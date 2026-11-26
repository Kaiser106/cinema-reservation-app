package com.cinema.application.features.auth.commands.logout;

import com.cinema.application.cqrs.CommandHandler;
import com.cinema.application.features.auth.ports.SessionAuthPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LogoutUserCommandHandler implements CommandHandler<LogoutUserCommand, Void> {
    private final SessionAuthPort sessionAuthPort;

    public LogoutUserCommandHandler(SessionAuthPort sessionAuthPort) {
        this.sessionAuthPort = sessionAuthPort;
    }

    @Override
    @Transactional
    public Void handle(LogoutUserCommand command) {
        sessionAuthPort.invalidate(command.sessionToken());
        return null;
    }
}
