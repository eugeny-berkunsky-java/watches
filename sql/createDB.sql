drop database if exists watches;
drop role if exists watches;

create role watches with login password 'watches';

create database watches
    with
    OWNER = watches
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
