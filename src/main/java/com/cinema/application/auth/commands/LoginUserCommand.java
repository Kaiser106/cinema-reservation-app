package com.cinema.application.auth.commands;

import com.cinema.application.common.Command;

public record LoginUserCommand(String email, String password, String userAgent, String ipAddress) implements Command<String> {}
