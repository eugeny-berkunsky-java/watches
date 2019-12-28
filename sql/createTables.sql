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

---

---
drop table if exists public."Customer";

create table public."Customer"
(
    id              serial         not null primary key,
    name            varchar        not null,
    sumOfOrders     decimal(15, 2) not null                                       default 0.00,
    discountCard_id integer        not null references public."DiscountCard" (id) default 0
);
alter table public."DiscountCard"
    owner to watches;

---
drop table if exists public."Order";
create table public."Order"
(
    id          serial                      not null primary key,
    date        timestamp without time zone not null default now(),
    customer_id int                         not null references public."Customer" (id),
    totalPrice  decimal(15, 2)              not null default 0.00
);
alter table public."Order"
    owner to watches;

---
drop index if exists item_order_watch_unique;
drop table if exists public."Item";
create table public."Item"
(
    id       serial         not null primary key,
    order_id integer        not null references public."Order" (id),
    watch_id integer        not null references public."Watch" (id),
    qty      integer        not null default 1,
    price    decimal(15, 2) not null default 0.00
);
create unique index item_order_watch_unique on public."Item" (order_id, watch_id);