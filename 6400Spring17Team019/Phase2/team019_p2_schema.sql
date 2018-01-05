


DROP DATABASE IF EXISTS cs6400_sp17_team019;


CREATE DATABASE cs6400_sp17_team019
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
 
-- posgres command
\c cs6400_sp17_team019;



CREATE TABLE site (
    siteid serial  NOT NULL,
    email varchar(250) NOT NULL UNIQUE,
    shortname varchar(250) NOT NULL,
    phonenumber bigint UNIQUE,
    streetaddress varchar(100) NOT NULL,
    city varchar(100) NOT NULL,
    statename varchar(100) NOT NULL,
    zipcode int,
    PRIMARY KEY (siteid)  
);


CREATE TABLE "user" (
    username varchar(100) NOT NULL UNIQUE,
    email varchar(250) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    firstname varchar(100) NOT NULL,
    lastname varchar(100),
    siteid int NOT NULL references site(siteid),
    PRIMARY KEY (username)  
);



CREATE TYPE status AS ENUM ('active', 'inactive');

CREATE TABLE servicefoodpantry (
    siteid int NOT NULL UNIQUE  references site(siteid),
    currstate status,
    description varchar(500),
    conditions varchar(500),
    starttime time,
    endtime time
   
);


CREATE TABLE servicesoupkitchen (
    siteid int NOT NULL UNIQUE  references site(siteid),
    currstate status,
    description varchar(500),
    conditions varchar(500),
    starttime time,
    endtime time,
    seats_available smallint CHECK(seats_available >= 0)
   
);

CREATE TABLE serviceshelter (
    siteid int NOT NULL references site(siteid),
    currstate status,
    description varchar(500),
    conditions varchar(500),
    starttime time,
    endtime time,
    malebunks smallint CHECK(malebunks >= 0),
    femalebunks smallint CHECK(femalebunks >= 0),
    mixedbunks smallint CHECK(mixedbunks >= 0),
    roomsavail smallint CHECK(roomsavail >= 0)
    
);

CREATE TABLE servicefoodbank (
    siteid int NOT NULL UNIQUE  references site(siteid),
    currstate status 
);





CREATE TYPE categories AS ENUM ('food', 'supplies');
CREATE TYPE subcategories AS ENUM ('personal_hygiene', 'clothing',
                                   'shelter', 'other',
                                   'vegetables', 'nuts/grains/beans',
                                   'meat/seafood', 'dairy/eggs',
                                   'sauce/condiments/seasoning',
                                   'juice/drink');
                                   
CREATE TYPE storage AS ENUM('drygood', 'refrigerated','frozen');
CREATE TABLE item (
    itemid serial NOT NULL UNIQUE,
    name varchar(40) NOT NULL,
    category categories NOT NULL,
    subcategory subcategories NOT NULL,
    storagetype storage NOT NULL,
    numunits int NOT NULL DEFAULT 1,
    expiry date NOT NULL DEFAULT '01/01/9999',
    siteid int NOT NULL references site(siteid),
    CONSTRAINT numunitcheck CHECK(numunits>0)
);

CREATE TYPE req_status AS ENUM ('pending', 'closed');
CREATE TABLE request (
    requestid serial NOT NULL,
    itemid int NOT NULL references item(itemid),
    reqstate req_status NOT NULL,
    quantity smallint NOT NULL,
    destsiteid int NOT NULL references site(siteid),
    username varchar(50) NOT NULL references "user"(username),
    PRIMARY KEY (requestid),  
    CONSTRAINT quantitycheck CHECK (quantity>0)
);



CREATE TYPE idprooftype AS ENUM ('driving_license', 'ssn', 'passport', 'birth_certificate', 'refugee_travel_document', 'visa' );
CREATE TABLE client (
    clientid serial NOT NULL,
    idnumber varchar(100) NOT NULL,
    idtype idprooftype NOT NULL,
    firstname varchar(100) NOT NULL,
    lastname varchar(100) NOT NULL,
    phonenumber bigint,
    PRIMARY KEY (clientid),
    UNIQUE(idnumber, idtype)
   
);

CREATE TABLE clientlogs (
    clientid int NOT NULL references client(clientid),
    timest timestamp NOT NULL DEFAULT now(),
    description varchar(100) NOT NULL,
    PRIMARY KEY (clientid, timest)
);


CREATE TABLE waitlist (
    siteid int NOT NULL references site(siteid),
    clientid int NOT NULL references client(clientid),
    waitlistnum int CHECK(waitlistnum > 0),
    PRIMARY KEY(siteid, clientid),
    UNIQUE(siteid, waitlistnum)
);

