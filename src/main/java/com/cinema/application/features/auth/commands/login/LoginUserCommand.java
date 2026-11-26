package com.cinema.application.features.auth.commands.login;

import com.cinema.application.cqrs.Command;

public record LoginUserCommand(String email, String password, String userAgent, String ipAddress) implements Command<String> {}
