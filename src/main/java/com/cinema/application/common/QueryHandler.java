package com.cinema.application.common;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
