drop trigger if exists trigger_update_customer_model on public."CustomerModel";
drop function if exists public.update_customer_model;

create or replace function public.update_customer_model() returns trigger as
$$
declare
    result_row record;
begin
    if tg_op = 'DELETE' then
        delete from public."Customer" where id = old.customer_id;

        if not found then
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
            sumoforders     = new.customer_sumoforders,
            discountcard_id = new.dcard_id
        where id = new.customer_id;

        if not found then
            return null;
        else
            return new;
        end if;

    end if;


end;
$$ language plpgsql;

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
        delete from public."Vendor" where id = old.vendor_id;
        if not found then
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

        if not found then
            return null;
        else
            return new;
        end if;
    end if;
end
$$ language plpgsql;

create trigger trigger_update_vendor_model
    instead of insert or update or delete
    on public."VendorModel"
    for each row
execute function public.update_vendor_model();