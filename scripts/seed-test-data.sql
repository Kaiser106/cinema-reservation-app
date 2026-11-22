-- Filmler (3 adet)
insert into movies (id, title, duration_minutes, genre, rating, poster_url)
select gen_random_uuid(), v.title, v.dur, v.genre, v.rating, null
from (values
          ('Inception',     148, 'Bilim Kurgu', '13+'),
          ('Kuru Otlar Üstüne', 197, 'Dram',     '13+'),
          ('Toy Story',      81, 'Animasyon',   'Genel')
     ) as v(title, dur, genre, rating)
where not exists (select 1 from movies m where m.title = v.title);

-- Seanslar: her salona 2 seans (yarın 14:00 ve 20:00), filmler salonlara sırayla dağıtılır
with hm as (
    select id as hall_id, row_number() over (order by id) as rn from halls
), mv as (
    select id, duration_minutes, row_number() over (order by title) as rn from movies
)
insert into sessions (id, movie_id, hall_id, start_time, end_time, price)
select gen_random_uuid(), mv.id, hm.hall_id, s.st,
       s.st + make_interval(mins => mv.duration_minutes), s.price
from hm
         join mv on mv.rn = ((hm.rn - 1) % (select count(*) from movies)) + 1
         cross join lateral (values
                                 (date_trunc('day', now()) + interval '1 day 14 hours', 150.00),
                                 (date_trunc('day', now()) + interval '1 day 20 hours', 180.00)
    ) as s(st, price)
where not exists (
    select 1 from sessions x
    where x.hall_id = hm.hall_id and x.start_time = s.st
);


-- Ürünler (3 adet)
insert into products (name, price, stock) values
                                              ('Büyük Mısır', 120.00, 50),
                                              ('Kola',         60.00, 100),
                                              ('Nachos',       90.00, 30)
on conflict (name) do nothing;

-- Roller
update users set role = 'ADMIN' where email = 'admin@test.com';
update users set role = 'STAFF' where email = 'staff@test.com';

-- Müşteriye başlangıç puanı
insert into user_points (user_id, balance)
select id, 100 from users where email = 'customer@test.com'
on conflict (user_id) do update set balance = excluded.balance;



