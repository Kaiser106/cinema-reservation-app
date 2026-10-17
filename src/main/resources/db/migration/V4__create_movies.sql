create table movies (
  id uuid primary key,
  title varchar(200) not null,
  duration_minutes int not null check (duration_minutes > 0),
  genre varchar(100) not null,
  rating varchar(20) not null,
  poster_url varchar(1000)
);
