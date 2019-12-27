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
truncate public."DiscountCard" cascade;
insert into public."DiscountCard" (id, number, percent)
VALUES (0, '630463531361029455', 0.00),
       (1, '630463531362029455', 0.50),
       (2, '560224446078347372', 1.00),
       (3, '633110590953141129', 5.00),
       (4, '503800880461066051', 10.00),
       (5, '560223775511216332', 15.00);
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

--
truncate public."Customer";
insert into public."Customer" (id, name, sumoforders, discountcard_id)
VALUES (1, 'Phil Patching', 0.0, 3)
     , (2, 'Hetty Plait', 0.0, 1)
     , (3, 'Nannette Mee', 0.0, 0)
     , (4, 'Filmer Croke', 0.0, 2)
     , (5, 'Siouxie Drews', 0.0, 4)
     , (6, 'Trent Brisseau', 0.0, 1)
     , (7, 'Lyndsie Ferrick', 0.0, 4)
     , (8, 'Maiga Bambrugh', 0.0, 0)
     , (9, 'Gasper D''Ruel', 0.0, 5)
     , (10, 'Maxi Albiston', 0.0, 0)
     , (11, 'Zedekiah Cassels', 0.0, 4)
     , (12, 'Noland Tarquini', 0.0, 4)
     , (13, 'Ellissa Chiswell', 0.0, 5)
     , (14, 'Thornie Gillogley', 0.0, 1)
     , (15, 'Maren Frederick', 0.0, 3)
     , (16, 'Ernest Gosnoll', 0.0, 5)
     , (17, 'Arri Senchenko', 0.0, 4)
     , (18, 'Devina Moncaster', 0.0, 2)
     , (19, 'Horton Sennett', 0.0, 1)
     , (20, 'Lucienne Stocken', 0.0, 1)
     , (21, 'Bruno Jankovic', 0.0, 2)
     , (22, 'Taddeusz Grinvalds', 0.0, 3)
     , (23, 'Scot Whapham', 0.0, 4)
     , (24, 'Clare Beauman', 0.0, 2)
     , (25, 'Louise Cordaroy', 0.0, 5)
     , (26, 'Sherill Clemerson', 0.0, 2)
     , (27, 'Rustin Corns', 0.0, 2)
     , (28, 'Wes Geard', 0.0, 4)
     , (29, 'Zora Fevier', 0.0, 1)
     , (30, 'Carie Borges', 0.0, 4)
     , (31, 'Roana Penhaleurack', 0.0, 0)
     , (32, 'Heindrick Christopher', 0.0, 0)
     , (33, 'Allin Cleaveland', 0.0, 2)
     , (34, 'Heather Haysman', 0.0, 3)
     , (35, 'Dana O''Reilly', 0.0, 2);
alter sequence public."Customer_id_seq" restart with 36;