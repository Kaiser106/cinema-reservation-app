package com.cinema.application.features.auth.commands.register;

import com.cinema.application.cqrs.Command;

public record RegisterUserCommand(String email, String password, String fullName, String phone) implements Command<String> {}
