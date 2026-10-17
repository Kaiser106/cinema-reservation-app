package com.cinema.application.auth.commands;

import com.cinema.application.common.Command;

public record RegisterUserCommand(String email, String password, String fullName, String phone) implements Command<String> {}
