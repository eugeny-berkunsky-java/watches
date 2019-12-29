drop view if exists "OrderModel";
drop view if exists public."ItemModel";
drop view if exists public."WatchModel";
drop view if exists public."CustomerModel";
drop view if exists public."VendorModel";
drop view if exists public."CountryModel";
drop view if exists public."DiscountCardModel";
----
create view public."CountryModel" as
select c.id   as c_id,
       c.name as c_name
from public."Country" c;

----
create view public."DiscountCardModel" as
select dc.id      as dc_id,
       dc.number  as dc_number,
       dc.percent as dc_percent
from public."DiscountCard" dc;

----
create view public."VendorModel" as
select v.id         as v_id,
       v.vendorname as v_name,
       cm.c_id      as v_c_id,
       cm.c_name    as v_c_name
from public."Vendor" v
         inner join public."CountryModel" cm on v.countryid = cm.c_id;

----
create view public."CustomerModel" as
select c.id           as c_id,
       c.name         as c_name,
       c.sumoforders  as c_sumoforders,
       dcm.dc_id      as c_dc_id,
       dcm.dc_number  as c_dc_number,
       dcm.dc_percent as c_dc_percent
from public."Customer" c
         inner join public."DiscountCardModel" dcm on c.discountcard_id = dcm.dc_id;

----
create view public."WatchModel" as
select w.id        as w_id,
       w.brand     as w_brand,
       w.type      as w_type,
       w.price     as w_price,
       w.qty       as w_qty,
       vm.v_id     as w_v_id,
       vm.v_name   as w_v_name,
       vm.v_c_id   as w_v_c_id,
       vm.v_c_name as w_v_c_name
from "Watch" w
         inner join public."VendorModel" vm on w.vendor_id = vm.v_id;


----
create view public."ItemModel" as
select i.id          as i_id,
       i.price       as i_price,
       i.qty         as i_qty,
       i.order_id    as i_order_id,
       wm.w_id       as i_w_id,
       wm.w_brand    as i_w_brand,
       wm.w_type     as i_w_type,
       wm.w_price    as i_w_price,
       wm.w_qty      as i_w_qty,
       wm.w_v_id     as i_w_v_id,
       wm.w_v_name   as i_w_v_name,
       wm.w_v_c_id   as i_w_v_c_id,
       wm.w_v_c_name as i_w_v_c_name
from public."Item" i
         inner join public."WatchModel" wm on i.watch_id = wm.w_id;

----
create view public."OrderModel" as
select
    -- order fields
    o.id             as o_id,
    o.date           as o_date,
    o.totalprice     as o_totalprice,

    -- customer fields
    cm.c_id          as o_c_id,
    cm.c_name        as o_c_name,
    cm.c_sumoforders as o_c_sumoforders,

    -- discount card fields
    cm.c_dc_id       as o_c_dc_id,
    cm.c_dc_number   as o_c_dc_number,
    cm.c_dc_percent  as o_c_dc_percent,

    -- item fields
    im.i_id          as o_i_id,
    im.i_price       as o_i_price,
    im.i_qty         as o_i_qty,
    im.i_order_id    as o_i_order_id,

    -- watch fields
    im.i_w_id        as o_i_w_id,
    im.i_w_brand     as o_i_w_brand,
    im.i_w_type      as o_i_w_type,
    im.i_w_price     as o_i_w_price,
    im.i_w_qty       as o_i_w_qty,

    -- vendor fields
    im.i_w_v_id      as o_i_w_v_id,
    im.i_w_v_name    as o_i_w_v_name,

    --country fields
    im.i_w_v_c_id    as o_i_w_v_c_id,
    im.i_w_v_c_name  as o_i_w_v_c_name

from public."Order" o
         inner join public."CustomerModel" cm on o.customer_id = cm.c_id
         inner join public."ItemModel" im on o.id = im.i_order_id;