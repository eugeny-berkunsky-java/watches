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