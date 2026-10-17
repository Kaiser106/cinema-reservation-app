-- Optimizes lookup for login and duplicate-user prevention
create index idx_users_email on users(email);

-- Optimizes customer profile fetch by user id
create index idx_customers_user_id on customers(user_id);

-- Optimizes session listing by movie and chronological browsing
create index idx_sessions_movie_start on sessions(movie_id, start_time);

-- Optimizes reservation history endpoint pagination for a user
create index idx_reservations_user_created on reservations(user_id, created_at desc);

-- Optimizes fast seat-reservation conflict check in create reservation flow
create index idx_reservation_seats_session_seat on reservation_seats(session_id, seat_id);

-- Enforces payment idempotency at DB level
create unique index uq_payments_idempotency_key on payments(idempotency_key);

-- Optimizes auth filter session validation path
create index idx_user_sessions_token_expiry on user_sessions(session_token, expires_at);

-- Optimizes outbox polling on pending and retry schedule
create index idx_outbox_pending_next_attempt on outbox_events(status, next_attempt_at, created_at);
