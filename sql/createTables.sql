set search_path to public;

-----
drop table if exists public."Country";
create table public."Country"
(
    id   serial  not null primary key,
    name varchar not null unique
);
ALTER TABLE public."Country"
    OWNER to watches;

-----
drop table if exists public."Vendor";
create table public."Vendor"
(
    id         serial  not null primary key,
    vendorName varchar not null unique,
    countryId  integer not null references public."Country" (id)
);
alter table public."Vendor"
    owner to watches;
---
drop table if exists public."DiscountCard";
create table public."DiscountCard"
(
    id      serial         not null primary key,
    number  varchar        not null unique,
    percent decimal(15, 2) not null default 0.00
);
alter table public."DiscountCard"
    owner to watches;

---
drop table if exists public."Watch";
drop type if exists public.watch_type;

create type watch_type as enum ('ANALOGUE', 'DIGITAL');

create table public."Watch"
(
    id        serial         not null primary key,
    brand     varchar        not null,
    type      watch_type     not null,
    price     decimal(15, 2) not null default 0.00,
    qty       integer        not null default 1,
    vendor_id integer        not null references public."Vendor" (id)
);
alter table public."Watch"
    owner to watches;
