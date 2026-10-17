create table customers (
  id uuid primary key,
  user_id uuid not null unique references users(id),
  full_name varchar(120) not null,
  phone varchar(30) not null,
  created_at timestamptz not null
);

create table theatre_info (
  id uuid primary key,
  name varchar(200) not null,
  address varchar(500) not null
);
