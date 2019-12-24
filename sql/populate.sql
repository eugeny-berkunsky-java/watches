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