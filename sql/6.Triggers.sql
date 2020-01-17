drop trigger if exists trigger_update_customer_model on public."CustomerModel";
drop function if exists public.update_customer_model;
create or replace function public.update_customer_model() returns trigger as
$$
declare
    result_row record;
begin
    if tg_op = 'DELETE' then
        update public."Customer"
        set deleted = true
        where id = old.customer_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;

    elsif tg_op = 'INSERT' then
        if NEW.customer_sumoforders is null then
            NEW.customer_sumoforders := 0.00;
        end if;
        if NEW.dcard_id is null then
            NEW.dcard_id := 0;
        end if;

        insert into public."Customer" (name, sumoforders, discountcard_id)
        values (NEW.customer_name, NEW.customer_sumoforders, NEW.dcard_id)
        returning id into NEW.customer_id;

        select * into result_row from public."CustomerModel" where customer_id = NEW.customer_id;

        return result_row;

    elsif tg_op = 'UPDATE' then
        update public."Customer"
        set name            = new.customer_name,
            discountcard_id = new.dcard_id
        where id = new.customer_id;

        if not FOUND then
            return null;
        else
            return new;
        end if;

    end if;


end;
$$ language plpgsql;

alter function update_customer_model() owner to watches;


create trigger trigger_update_customer_model
    instead of insert or update or delete
    on public."CustomerModel"
    for each row
execute function public.update_customer_model();

-----------------------------------------------------------
drop trigger if exists trigger_update_vendor_model on public."VendorModel";
drop function if exists public.update_vendor_model;

create or replace function public.update_vendor_model() returns trigger as
$$
declare
    result_row record;
begin
    if tg_op = 'DELETE' then
        update public."Vendor"
        set deleted = true
        where id = old.vendor_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;

    elsif tg_op = 'INSERT' then
        insert into public."Vendor" (vendorname, countryid)
        VALUES (new.vendor_name, new.country_id)
        returning id into new.vendor_id;

        select * into result_row from public."VendorModel" where vendor_id = new.vendor_id;
        return result_row;

    elsif tg_op = 'UPDATE' then
        update public."Vendor"
        set vendorname = new.vendor_name,
            countryid  = new.country_id
        where id = new.vendor_id;

        if not FOUND then
            return null;
        else
            return new;
        end if;
    end if;
end
$$ language plpgsql;

alter function update_vendor_model() owner to watches;

create trigger trigger_update_vendor_model
    instead of insert or update or delete
    on public."VendorModel"
    for each row
execute function public.update_vendor_model();

-----------------------------------------------------------
drop trigger if exists trigger_update_watch_model on public."WatchModel";
drop function if exists public.update_watch_model;

create or replace function public.update_watch_model() returns trigger as
$$
declare
    result_row record;
begin
    if tg_op = 'DELETE' then
        update public."Watch"
        set deleted = true
        where id = old.watch_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;

    elsif tg_op = 'INSERT' then
        new.watch_price := coalesce(new.watch_price, 0.00);
        new.watch_qty := coalesce(new.watch_qty, 1);

        insert into public."Watch" (brand, type, price, qty, vendor_id)
        values (new.watch_brand, new.watch_type, new.watch_price, new.watch_qty, new.vendor_id)
        returning id into new.watch_id;

        select * into result_row from public."WatchModel" where watch_id = new.watch_id;

        return result_row;

    elsif tg_op = 'UPDATE' then

        update public."Watch"
        set brand     = new.watch_brand,
            type      = new.watch_type,
            price     = new.watch_price,
            qty= new.watch_qty,
            vendor_id = new.vendor_id
        where id = new.watch_id;

        if not FOUND then
            return null;
        else
            return new;
        end if;

    end if;
end
$$ language plpgsql;

alter function update_watch_model() owner to watches;

create trigger trigger_update_watch_model
    instead of insert or update or delete
    on public."WatchModel"
    for each row
execute function public.update_watch_model();


-----------------------------------------------------------
drop trigger if exists trigger_update_item_model on public."ItemModel";
drop function if exists public.update_item_model;
create or replace function public.update_item_model() returns trigger as
$$
declare
    result_row record;
begin

    if tg_op = 'DELETE' then
        update public."Item"
        set deleted = true
        where id = old.item_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;

    elsif tg_op = 'INSERT' then
        new.item_qty := coalesce(new.item_qty, 1);
        new.item_price := coalesce(new.item_price, 0.00);

        insert into public."Item" (order_id, qty, price, watch_id)
        values (new.item_order_id, new.item_qty, new.item_price, new.watch_id)
        returning id into new.item_id;

        select * into result_row from public."ItemModel" where item_id = new.item_id;

        return result_row;

    elsif tg_op = 'UPDATE' then
        update public."Item"
        set price = new.item_price,
            qty   = new.item_qty
        where id = new.item_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;
    end if;
end
$$ language plpgsql;

alter function update_item_model() owner to watches;

create trigger trigger_update_item_model
    instead of insert or update or delete
    on public."ItemModel"
    for each row
execute function public.update_item_model();


-----------------------------------------------------------
drop trigger if exists trigger_update_order_model on public."OrderModel";
drop function if exists public.update_order_model;
create or replace function public.update_order_model() returns trigger as
$$
declare
    result_row record;
begin
    if tg_op = 'DELETE' then
        update public."Order"
        set deleted = true
        where id = old.order_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;

    elsif tg_op = 'INSERT' then

        new.order_date := coalesce(new.order_date, localtimestamp);
        new.order_totalprice := coalesce(new.order_totalprice, 0.00);

        insert into public."Order" (date, customer_id, totalprice)
        values (new.order_date, new.customer_id, new.order_totalprice)
        returning id into new.order_id;

        select * into result_row from public."OrderModel" where order_id = new.order_id;

        return result_row;

    elsif tg_op = 'UPDATE' then
        update public."Order"
        set date       = new.order_date,
            totalprice = new.order_totalprice
        where id = old.order_id;

        if not FOUND then
            return null;
        else
            return old;
        end if;

    end if;
end
$$ language plpgsql;

alter function update_order_model() owner to watches;

create trigger trigger_update_order_model
    instead of insert or update or delete
    on public."OrderModel"
    for each row
execute function public.update_order_model();

-----------------------------------------------------------
drop trigger if exists trigger_update_country_model on public."CountryModel";
drop function if exists public.update_country_model;
create or replace function public.update_country_model() returns trigger as
$$
begin
    update public."Country"
    set deleted = true
    where id = old.country_id;

    if not FOUND then
        return null;
    else
        return old;
    end if;
end
$$ language plpgsql;

alter function update_country_model() owner to watches;

create trigger trigger_update_country_model
    instead of delete
    on public."CountryModel"
    for each row
execute function public.update_country_model();

-----------------------------------------------------------
drop trigger if exists trigger_update_dcard_model on public."DiscountCardModel";
drop function if exists public.update_dcard_model;
create or replace function public.update_dcard_model() returns trigger as
$$
begin
    update public."DiscountCard"
    set deleted = true
    where id = old.dcard_id;

    if not FOUND then
        return null;
    else
        return old;
    end if;
end
$$ language plpgsql;

alter function update_dcard_model() owner to watches;

create trigger trigger_update_dcard_model
    instead of delete
    on public."DiscountCardModel"
    for each row
execute function public.update_dcard_model();