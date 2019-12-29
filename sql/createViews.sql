drop view if exists "OrderModel";
drop view if exists public."ItemModel";
drop view if exists public."WatchModel";
drop view if exists public."CustomerModel";
drop view if exists public."VendorModel";
drop view if exists public."CountryModel";
drop view if exists public."DiscountCardModel";
----
create view public."CountryModel" as
select c.id   as country_id,
       c.name as country_name
from public."Country" c;

----
create view public."DiscountCardModel" as
select dc.id      as dcard_id,
       dc.number  as dcard_number,
       dc.percent as dcard_percent
from public."DiscountCard" dc;

----
create view public."VendorModel" as
select v.id         as vendor_id,
       v.vendorname as vendor_name,
       cm.*
from public."Vendor" v
         inner join public."CountryModel" cm on v.countryid = cm.country_id;

----
create view public."CustomerModel" as
select c.id          as customer_id,
       c.name        as customer_name,
       c.sumoforders as customer_sumoforders,
       dcm.*
from public."Customer" c
         inner join public."DiscountCardModel" dcm on c.discountcard_id = dcm.dcard_id;

----
create view public."WatchModel" as
select w.id    as watch_id,
       w.brand as watch_brand,
       w.type  as watch_type,
       w.price as watch_price,
       w.qty   as watch_qty,
       vm.*
from "Watch" w
         inner join public."VendorModel" vm on w.vendor_id = vm.vendor_id;


----
create view public."ItemModel" as
select i.id       as item_id,
       i.price    as item_price,
       i.qty      as item_qty,
       i.order_id as item_order_id,
       wm.*
from public."Item" i
         inner join public."WatchModel" wm on i.watch_id = wm.watch_id;

----
create view public."OrderModel" as
select
    -- order fields
    o.id         as order_id,
    o.date       as order_date,
    o.totalprice as order_totalprice,
    cm.*,
    im.*
from public."Order" o
         inner join public."CustomerModel" cm on o.customer_id = cm.customer_id
         inner join public."ItemModel" im on o.id = im.item_order_id;