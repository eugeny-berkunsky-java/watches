alter table if exists public."Item"
    add constraint item_qty_price_check check (
        qty > 0 and price > 0.00
        );