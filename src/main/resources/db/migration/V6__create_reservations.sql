create table reservations (
  id uuid primary key,
  user_id uuid not null references users(id),
  session_id uuid not null references sessions(id),
  status varchar(20) not null check (status in ('PENDING','CONFIRMED','CANCELLED','EXPIRED')),
  total_price numeric(12,2) not null check (total_price >= 0),
  created_at timestamptz not null,
  expires_at timestamptz not null,
  deleted_at timestamptz
);

create table reservation_seats (
  id uuid primary key,
  seat_id bigint not null references seats(id),
  reservation_id uuid not null references reservations(id),
  session_id uuid not null references sessions(id),
  price numeric(12,2) not null check (price >= 0),
  version bigint not null default 0
);
