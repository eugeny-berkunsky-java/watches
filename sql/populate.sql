truncate public."Country";

INSERT INTO public."Country" (id, name)
VALUES (1, 'Italy'),
       (2, 'France'),
       (3, 'UK'),
       (4, 'China');

alter sequence public."Country_id_seq" restart with 5;

----
truncate public."Vendor";
insert into public."Vendor" (id, vendorname, countryid)
values (1, 'Cartier', 1),
       (2, 'Bell & Ross', 2),
       (3, 'Bremont', 3),
       (4, 'Beijing Watch Factory', 4);

alter sequence public."Vendor_id_seq" restart with 5;

--
truncate public."DiscountCard";
insert into public."DiscountCard" (id, number, percent)
VALUES (0, '630463531361029455', 0.00),
       (1, '630463531361029455', 0.50),
       (3, '560224446078347372', 1.00),
       (2, '633110590953141129', 5.00),
       (3, '503800880461066051', 10.00),
       (4, '560223775511216332', 15.00);
alter sequence public."DiscountCard_id_seq" restart with 7;

--
truncate public."Watch";
insert into public."Watch" (id, brand, type, price, qty, vendor_id)
values (1, 'Model 1', 'ANALOGUE', 10.00, 1, 1),
       (2, 'Model 2', 'DIGITAL', 200.00, 10, 1),
       (3, 'Model 3', 'ANALOGUE', 50.00, 10, 2),
       (4, 'Model 4', 'DIGITAL', 200.00, 10, 2),
       (5, 'Model 5', 'ANALOGUE', 500.00, 10, 3),
       (6, 'Model 6', 'DIGITAL', 100.00, 10, 3),
       (7, 'Model 7', 'DIGITAL', 100.00, 10, 3);
alter sequence public."Watch_id_seq" restart with 8;