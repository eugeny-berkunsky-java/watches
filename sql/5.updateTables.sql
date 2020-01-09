alter table if exists public."Country"
    add column if not exists deleted boolean not null default FALSE;
alter table if exists public."Customer"
    add column if not exists deleted boolean not null default FALSE;
alter table if exists public."DiscountCard"
    add column if not exists deleted boolean not null default FALSE;
alter table if exists public."Item"
    add column if not exists deleted boolean not null default FALSE;
alter table if exists public."Order"
    add column if not exists deleted boolean not null default FALSE;
alter table if exists public."Vendor"
    add column if not exists deleted boolean not null default FALSE;
alter table if exists public."Watch"
    add column if not exists deleted boolean not null default FALSE;

alter table if exists public."Item"
    add constraint item_qty_price_check check (
        qty > 0 and price > 0.00
        );