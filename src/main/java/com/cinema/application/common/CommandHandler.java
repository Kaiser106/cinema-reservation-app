package com.cinema.application.common;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
}
