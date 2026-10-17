package com.cinema.application.auth;

import com.cinema.application.auth.commands.LogoutUserCommand;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.port.SessionAuthPort;
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
