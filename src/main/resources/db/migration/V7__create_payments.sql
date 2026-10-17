create table payments (
  id uuid primary key,
  amount numeric(12,2) not null check (amount >= 0),
  reservation_id uuid not null references reservations(id),
  payment_method varchar(50) not null,
  status varchar(20) not null check (status in ('PENDING','COMPLETED','FAILED')),
  paid_at timestamptz,
  transaction_ref varchar(200),
  idempotency_key varchar(120) not null unique,
  created_at timestamptz not null
);
