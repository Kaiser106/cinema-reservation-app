create table users (
  id uuid primary key,
  email varchar(200) not null unique,
  password_hash varchar(200) not null,
  role varchar(20) not null check (role in ('ADMIN','CUSTOMER','STAFF')),
  active boolean not null,
  created_at timestamptz not null
);
