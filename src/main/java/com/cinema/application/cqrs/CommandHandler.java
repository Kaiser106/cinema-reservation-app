package com.cinema.application.cqrs;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
}
