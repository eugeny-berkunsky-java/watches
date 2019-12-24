
set search_path to public;

DROP TABLE if exists public."Country";

CREATE TABLE public."Country"
(
    id serial not null primary key,
    name varchar not null unique
);

ALTER TABLE public."Country" OWNER to watches;
