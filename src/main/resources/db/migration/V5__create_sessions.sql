create table sessions (
  id uuid primary key,
  movie_id uuid not null references movies(id),
  hall_id bigint not null references halls(id),
  start_time timestamptz not null,
  end_time timestamptz not null,
  price numeric(12,2) not null check (price >= 0),
  check (end_time > start_time)
);
