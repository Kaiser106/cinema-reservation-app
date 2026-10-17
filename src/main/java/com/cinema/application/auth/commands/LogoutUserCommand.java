package com.cinema.application.auth.commands;

import com.cinema.application.common.Command;

public record LogoutUserCommand(String sessionToken) implements Command<Void> {}
