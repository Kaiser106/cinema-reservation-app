package com.cinema.application.features.auth.commands.logout;

import com.cinema.application.cqrs.Command;

public record LogoutUserCommand(String sessionToken) implements Command<Void> {}
