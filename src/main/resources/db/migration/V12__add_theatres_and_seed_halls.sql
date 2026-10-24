-- 1) Şube tablosu
create table theatres (
                          id      bigserial primary key,
                          name    varchar(100) not null unique,
                          city    varchar(100) not null,
                          address varchar(255)
);

-- 2) Salonları şubeye bağla, salon adı şube içinde unique olsun
alter table halls add column theatre_id bigint references theatres(id);
alter table halls drop constraint halls_name_key;
alter table halls add constraint uq_halls_theatre_name unique (theatre_id, name);

-- 3) 2 şube
insert into theatres (name, city, address) values
                                               ('Kadıköy Şubesi', 'İstanbul', 'Kadıköy, İstanbul'),
                                               ('Çankaya Şubesi', 'Ankara',   'Çankaya, Ankara');

-- 4) Her şubeye 3 salon (10 sıra)
insert into halls (name, row_count, theatre_id)
select 'Salon ' || h, 10, t.id
from theatres t
         cross join generate_series(1, 3) as h;

-- 5) Her salona 100 koltuk: A-J sıraları x 1-10 numara
insert into seats (row_label, seat_number, hall_id, type)
select chr(64 + r), s, hl.id, 'STANDARD'
from halls hl
         cross join generate_series(1, 10) as r
         cross join generate_series(1, 10) as s
where hl.theatre_id is not null;

-- 6) Bundan sonra her salon bir şubeye ait olmak zorunda
alter table halls alter column theatre_id set not null;
