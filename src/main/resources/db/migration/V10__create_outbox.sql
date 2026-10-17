create table outbox_events (
  id uuid primary key,
  event_type varchar(100) not null,
  payload text not null,
  status varchar(20) not null check (status in ('PENDING','PROCESSING','PROCESSED','DEAD')),
  retry_count int not null default 0,
  next_attempt_at timestamptz,
  processed_at timestamptz,
  last_error text,
  created_at timestamptz not null
);
