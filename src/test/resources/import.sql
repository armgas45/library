create table if not exists users(id serial primary key,address varchar(255),country varchar(255),cvv varchar(255),email varchar(255) unique,expdate varchar(255),name varchar(255),pan varchar(255),password varchar(255),phone varchar(255),postal_zip varchar(255),role varchar(255));