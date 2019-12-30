truncate public."Country" cascade;
insert into public."Country" (id, name)
values (1, 'Italy'),
       (2, 'France'),
       (3, 'UK'),
       (4, 'China');

alter sequence public."Country_id_seq" restart with 5;

----
truncate public."Vendor" cascade;
insert into public."Vendor" (id, vendorname, countryid)
values (1, 'Cartier', 1),
       (2, 'Bell & Ross', 2),
       (3, 'Bremont', 3),
       (4, 'Beijing Watch Factory', 4);

alter sequence public."Vendor_id_seq" restart with 5;

----
truncate public."DiscountCard" cascade;
insert into public."DiscountCard" (id, number, percent)
VALUES (0, '630463531361029455', 0.00),
       (1, '630463531362029455', 0.50),
       (2, '560224446078347372', 1.00),
       (3, '633110590953141129', 5.00),
       (4, '503800880461066051', 10.00),
       (5, '560223775511216332', 15.00);
alter sequence public."DiscountCard_id_seq" restart with 7;

----
truncate public."Watch" cascade;
insert into public."Watch" (id, brand, type, price, qty, vendor_id)
values (1, 'Model 1', 'ANALOGUE', 10.00, 1, 1),
       (2, 'Model 2', 'DIGITAL', 200.00, 10, 1),
       (3, 'Model 3', 'ANALOGUE', 50.00, 10, 2),
       (4, 'Model 4', 'DIGITAL', 200.00, 10, 2),
       (5, 'Model 5', 'ANALOGUE', 500.00, 10, 3),
       (6, 'Model 6', 'DIGITAL', 100.00, 10, 3),
       (7, 'Model 7', 'DIGITAL', 100.00, 10, 3);
alter sequence public."Watch_id_seq" restart with 8;

----
truncate public."Customer" cascade;
insert into public."Customer" (id, name, sumoforders, discountcard_id)
VALUES (1, 'Phil Patching', 2086.80, 3),
       (2, 'Nannette Mee', 3625.00, 0),
       (3, 'Filmer Croke', 1308.30, 2),
       (4, 'Siouxie Drews', 5056.20, 4),
       (5, 'Maxi Albiston', 25.95, 1);
alter sequence public."Customer_id_seq" restart with 6;

----
truncate public."Order" cascade;
insert into public."Order" (id, date, customer_id, totalprice)
VALUES (1, '2018-04-17 09:00:23.000', 1, 1021.20),
       (2, '2019-05-02 15:21:23.000', 1, 1065.60),
       (3, '2018-05-02 10:00:52.000', 2, 1363.00),
       (4, '2019-05-02 10:00:52.000', 2, 2262.00),
       (5, '2018-05-05 11:00:52.000', 3, 1020.80),
       (6, '2019-05-05 11:00:52.000', 3, 287.50),
       (7, '2018-06-07 11:00:52.000', 4, 5056.20),
       (8, '2019-12-25 11:00:52.000', 5, 25.95),
       (9, '2018-10-11 09:00:00.000', 5, 0.00);
alter sequence public."Order_id_seq" restart with 10;

----
truncate public."Item" cascade;
insert into public."Item" (id, order_id, watch_id, qty, price)
values (1, 1, 2, 2, 210.00),
       (2, 1, 5, 1, 500.00),
       (3, 2, 1, 3, 10.00),
       (4, 2, 4, 2, 200.00),
       (5, 2, 6, 2, 115.00),
       (6, 2, 7, 2, 150.00),
       (7, 3, 2, 3, 205.00),
       (8, 3, 3, 1, 60.00),
       (9, 3, 5, 1, 500.00),
       (10, 4, 1, 5, 10.00),
       (11, 4, 2, 3, 200.00),
       (12, 4, 4, 3, 200.00),
       (13, 4, 6, 4, 100.00),
       (14, 4, 7, 3, 100.00),
       (15, 5, 1, 3, 10.00),
       (16, 5, 2, 3, 200.00),
       (17, 5, 3, 1, 50.00),
       (18, 5, 4, 1, 200.00),
       (19, 6, 6, 1, 120.00),
       (20, 6, 7, 1, 130.00),
       (21, 7, 1, 2, 10.00),
       (22, 7, 2, 4, 200.00),
       (23, 7, 3, 3, 50.00),
       (24, 7, 4, 4, 200.00),
       (25, 7, 5, 5, 500.00),
       (26, 7, 6, 2, 100.00),
       (27, 7, 7, 3, 100.00),
       (28, 8, 1, 1, 15.75);
alter sequence public."Item_id_seq" restart with 29;
