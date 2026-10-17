create table halls (
  id bigserial primary key,
  name varchar(100) not null unique,
  row_count int not null check (row_count > 0)
);

create table seats (
  id bigserial primary key,
  row_label varchar(10) not null,
  seat_number int not null check (seat_number > 0),
  hall_id bigint not null references halls(id),
  type varchar(20) not null check (type in ('STANDARD','VIP')),
  unique (hall_id, row_label, seat_number)
);
