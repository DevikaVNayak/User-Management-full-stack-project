-- Database: DemoProject

-- DROP DATABASE IF EXISTS "DemoProject";

CREATE DATABASE "DemoProject"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- admin_details table

create table table_admin
(
 	admin_key serial primary key,
 	admin_id varchar not null ,
 	admin_fname varchar(50) not null,
 	admin_mname varchar,
 	admin_lname varchar(50) not null,
 	admin_email varchar(250) not null,
 	admin_password varchar(250) not null,
 	admin_contact Numeric not null,
 	admin_createdAt timestamp ,
 	admin_address varchar(100)not null
);



-- user_details table

CREATE TABLE table_user (
    user_key SERIAL PRIMARY KEY,
    user_firstname VARCHAR(25) NOT NULL,
    user_middlename VARCHAR(25),
    user_lastname VARCHAR(25) NOT NULL,
    user_email VARCHAR(25) NOT NULL,
    user_contactnumber NUMERIC NOT NULL,
    user_gender VARCHAR(20) NOT NULL,
    user_dob DATE NOT NULL,
    user_address VARCHAR(200) NOT NULL,

	admin_key int not null,
	constraint fk_admin
	foreign key(admin_key)
	references table_Admin(admin_key)
	on delete cascade
	);
