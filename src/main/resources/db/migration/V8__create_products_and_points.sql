create table products (
  id bigserial primary key,
  name varchar(120) not null unique,
  price numeric(12,2) not null check (price >= 0),
  stock int not null check (stock >= 0)
);

create table user_points (
  user_id uuid primary key references users(id),
  balance int not null check (balance >= 0)
);
