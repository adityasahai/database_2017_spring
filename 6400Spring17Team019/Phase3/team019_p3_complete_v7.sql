    DROP DATABASE IF EXISTS cs6400_sp17_team019;


    CREATE DATABASE cs6400_sp17_team019
        WITH 
        OWNER = postgres
        ENCODING = 'UTF8'
        TABLESPACE = pg_default
        CONNECTION LIMIT = -1;
     
    -- posgres command
    \c cs6400_sp17_team019;

    drop table if exists clientlogs;
    drop table if exists request;
    DROP table if exists item;
    drop table if exists waitlist;
    drop table if exists servicefoodbank;
    drop table if exists servicefoodpantry;
    drop table if exists serviceshelter;
    drop table if exists servicesoupkitchen;
    drop table if exists client;
    drop table if exists "user";
    drop table if exists site;
    drop type if exists idprooftype;
    drop type if exists status;
    drop type if exists categories;
    drop type if exists subcategories;
    drop type if exists storage;
    drop type if exists req_status;



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
        siteid int NOT NULL UNIQUE references site(siteid),
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
        CONSTRAINT numunitcheck CHECK(numunits>=0)
    );

    CREATE TYPE req_status AS ENUM ('pending', 'closed');
    CREATE TABLE request (
        requestid serial NOT NULL,
        itemid int NOT NULL references item(itemid),
        reqstate req_status NOT NULL,
        quantity smallint NOT NULL,
        username varchar(50) NOT NULL references "user"(username),
        PRIMARY KEY (requestid),  
        CONSTRAINT quantitycheck CHECK (quantity>=0)
    );



    CREATE TYPE idprooftype AS ENUM ('driving_license', 'ssn', 'passport', 'birth_certificate', 'refugee_travel_document', 'visa' );
    CREATE TABLE client (
        clientid serial NOT NULL,
        idnumber varchar(100) NOT NULL,
        idtype idprooftype NOT NULL,
        firstname varchar(100) NOT NULL,
        lastname varchar(100) NOT NULL,
        phonenumber bigint default 0,
        PRIMARY KEY (clientid),
        UNIQUE(idnumber, idtype)
       
    );

    CREATE TABLE clientlogs (
        clientid int NOT NULL references client(clientid),
        timest timestamp NOT NULL DEFAULT now(),
        description varchar(500) NOT NULL,
        PRIMARY KEY (clientid, timest)
    );


    CREATE TABLE waitlist (
        siteid int NOT NULL references site(siteid),
        clientid int NOT NULL references client(clientid),
        waitlistnum int CHECK(waitlistnum > 0),
        PRIMARY KEY(siteid, clientid),
        UNIQUE(siteid, waitlistnum)
    );


    INSERT INTO site (email, shortname, streetaddress, city, statename, zipcode) 
    VALUES ('site1@asacs.com', 'site1', 'street1', 'atlanta',  'georgia', 30301);

    INSERT INTO site (email, shortname, streetaddress, city, statename, zipcode) 
    VALUES ('site2@asacs.com', 'site2', 'street2', 'atlanta',  'georgia', 30302);

    INSERT INTO site (email, shortname, streetaddress, city, statename, zipcode) 
    VALUES ('site3@asacs.com', 'site3', 'street3', 'atlanta',  'georgia', 30303);



    INSERT INTO "user" (username, email, siteid, firstname, lastname, password) 
    VALUES ('emp1', 'emp1@asacs.com', 1, 'Site1', 'Employee1', 'gatech123');

    INSERT INTO "user" (username, email, siteid, firstname, lastname, password) 
    VALUES ('emp2', 'emp2@asacs.com', 2, 'Site2', 'Employee2', 'gatech123');

    INSERT INTO "user" (username, email, siteid, firstname, lastname, password) 
    VALUES ('emp3', 'emp3@asacs.com', 3, 'Site3', 'Employee3', 'gatech123');

    INSERT INTO "user" (username, email, siteid, firstname, lastname, password) 
    VALUES ('vol1', 'vol1@asacs.com', 1, 'Demo', 'Volunteer1', 'gatech123');

    INSERT INTO "user" (username, email, siteid, firstname, lastname, password) 
    VALUES ('vol2', 'vol2@asacs.com', 2, 'Demo', 'Volunteer2', 'gatech123');

    INSERT INTO "user" (username, email, siteid, firstname, lastname, password) 
    VALUES ('vol3', 'vol3@asacs.com', 3, 'Demo', 'Volunteer3', 'gatech123');


    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456100001', 'driving_license', 'Joe', 'Client1');


    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456100002', 'driving_license', 'Joe', 'Client2');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456100003', 'driving_license', 'Joe', 'Client3');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456100004', 'driving_license', 'Joe', 'Client4');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456100005', 'driving_license', 'Joe', 'Client5');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456100006', 'driving_license', 'Joe', 'Client6');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456200001', 'driving_license', 'Jane', 'Client7');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456200002', 'driving_license', 'Jane', 'Client8');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456200003', 'driving_license', 'Jane', 'Client9');

    INSERT INTO client (idnumber, idtype,  firstname, lastname, phonenumber) 
    VALUES ('123456200004', 'driving_license', 'Jane', 'Client10', 4142890101);

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456200005', 'driving_license', 'Jane', 'Client11');

    INSERT INTO client (idnumber, idtype,  firstname, lastname) 
    VALUES ('123456200006', 'driving_license', 'Jane', 'Client12');


    INSERT INTO servicefoodpantry(siteid, currstate, description, conditions, starttime, endtime) 
    VALUES (1, 'active', 'pantry1', 'only evenings', '18:00', '22:00');

    INSERT INTO servicefoodpantry(siteid, currstate, description, conditions, starttime, endtime) 
    VALUES (2, 'inactive', 'pantry2', 'only evenings', '18:00', '22:00');

    INSERT INTO servicefoodpantry(siteid, currstate, description, conditions, starttime, endtime) 
    VALUES (3, 'active', 'pantry3', 'only evenings', '18:00', '22:00');


    INSERT INTO servicesoupkitchen(siteid, currstate, description, conditions, starttime, endtime, seats_available) 
    VALUES (1, 'inactive', 'soup1', 'only evenings', '18:00', '22:00', 20);

    INSERT INTO servicesoupkitchen(siteid, currstate, description, conditions, starttime, endtime, seats_available) 
    VALUES (2, 'active', 'soup2', 'only evenings', '18:00', '22:00', 30);

    INSERT INTO servicesoupkitchen(siteid, currstate, description, conditions, starttime, endtime, seats_available) 
    VALUES (3, 'active', 'soup3', 'only evenings', '18:00', '22:00', 40);

    INSERT INTO serviceshelter(siteid, currstate, description, conditions, malebunks, femalebunks, mixedbunks, roomsavail)
    VALUES (1, 'inactive', 'shelter1', 'all day', 4, 4, 4, 0);


    INSERT INTO serviceshelter(siteid, currstate, description, conditions, malebunks, femalebunks, mixedbunks, roomsavail) 
    VALUES (2, 'active', 'shelter2', 'all day', 4, 4, 4, 0);


    INSERT INTO serviceshelter(siteid, currstate, description, conditions, malebunks, femalebunks, mixedbunks, roomsavail) 
    VALUES (3, 'active', 'shelter3', 'all day', 4, 4, 4, 0);





    INSERT INTO servicefoodbank(siteid, currstate) VALUES(1, 'active');
    INSERT INTO servicefoodbank(siteid, currstate) VALUES(2, 'active');
    INSERT INTO servicefoodbank(siteid, currstate) VALUES(3, 'active');


    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES (1, 3, 1);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES (2, 3, 5);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES (3, 3, 6);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES (4, 3, 2);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES (5, 3, 10);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES (6, 3, 12);
                         
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES(1, 2, 5);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES(2, 2, 4);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES(3, 2, 3);
    INSERT INTO waitlist (waitlistnum, siteid, clientid) VALUES(4, 2, 8);



    INSERT INTO clientlogs(clientid, timest, description) VALUES(1, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(1, now()- INTERVAL '10 days 2 hours', 'visited foodpantry1');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(1, now()- INTERVAL '8 days 2 hours', 'meal provided.');



    INSERT INTO clientlogs(clientid, timest, description) VALUES(2, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(2, now()- INTERVAL '6 days 5 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(2, now()- INTERVAL '7 days 4 hours', 'provided meal');


    INSERT INTO clientlogs(clientid, timest, description) VALUES(3, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(3, now()- INTERVAL '11 days 3 hours', 'bunk provided');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(3, now()- INTERVAL '7 days 5 hours', 'provided meal');


    INSERT INTO clientlogs(clientid, timest, description) VALUES(4, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(4, now()- INTERVAL '10 days 2 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(4, now()- INTERVAL '3 days 14 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(4, now()- INTERVAL '8 days 15 hours', 'provided meal');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(5, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(5, now()- INTERVAL '5 days 22 hours', 'room alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(5, now()- INTERVAL '4 days 14 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(5, now()- INTERVAL '3 days 8 hours', 'profile updated, changed name from Smith to Jon');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(6, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(6, now()- INTERVAL '4 days 22 hours', 'bunk alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(6, now()- INTERVAL '6 days 23 hours', 'visited soupkitchen1');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(6, now()- INTERVAL '8 days 13 hours', 'visited soupkitchen3');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(7, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(7, now()- INTERVAL '2 days 12 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(7, now()- INTERVAL '11 days 16 hours', 'visited soupkitchen3');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(7, now()- INTERVAL '14 days 8 hours', 'room alloted');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(8, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(8, now()- INTERVAL '5 days 4 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(8, now()- INTERVAL '7 days 7 hours', 'room alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(8, now()- INTERVAL '12 days 12 hours', 'bunk alloted');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(9, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(9, now()- INTERVAL '12 days 14 hours', 'room alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(9, now()- INTERVAL '13 days 12 hours', 'bunk alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(9, now()- INTERVAL '4 days 2 hours', 'room alloted');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(10, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(10, now()- INTERVAL '6 days 1 hours', 'provided meal');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(10, now()- INTERVAL '12 days 4 hours', 'updated phonenumber');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(10, now()- INTERVAL '7 days 6 hours', 'room alloted');


    INSERT INTO clientlogs(clientid, timest, description) VALUES(11, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(11, now()- INTERVAL '12 days 7 hours', 'bunk alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(11, now()- INTERVAL '11 days 4 hours', 'visited soupkitchen2');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(11, now()- INTERVAL '9 days 4 hours', 'room alloted');

    INSERT INTO clientlogs(clientid, timest, description) VALUES(12, now()- INTERVAL '15 days', 'profile created');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(12, now()- INTERVAL '1 days 4 hours', 'bunk alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(12, now()- INTERVAL '3 days 6 hours', 'room alloted');
    INSERT INTO clientlogs(clientid, timest, description) VALUES(12, now()- INTERVAL '5 days 1 hours', 'visited soupkitchen1');





    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('spinach - leafy vegetable','food', 'vegetables', 'refrigerated', '2', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Lettuces - leafy vegetable','food', 'vegetables', 'refrigerated', '5', '1', now()+ INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Cruciferous Leafy Greens','food', 'vegetables', 'refrigerated', '3', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Dandelion - leafy vegetable','food', 'vegetables', 'refrigerated', '8', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('plantain - leafy vegetable','food', 'vegetables', 'refrigerated', '7', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('watercress  - leafy vegetable','food', 'vegetables', 'refrigerated', '4', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chickweed  - leafy vegetable','food', 'vegetables', 'refrigerated', '6', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Swiss Chard  - leafy vegetable','food', 'vegetables', 'refrigerated', '8', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Broccoli - leafy vegetable','food', 'vegetables', 'refrigerated', '1', '1', now()+ INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Caigua - leafy vegetable','food', 'vegetables', 'refrigerated', '4', '1', now()+ INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Acorn  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('almond nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Beech  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Breadnut  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Chestnuts  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Deeknut  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Hazelnuts  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Johnstone River almond  nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Palm nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Red bopple nut','food', 'nuts/grains/beans', 'drygood', '10', '1', now() + INTERVAL '60 days');




    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce1','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce2','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce3','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce4','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce5','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce6','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce7','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce8','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce9','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sauce10','food', 'sauce/condiments/seasoning', 'drygood', '10', '1', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose1','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose2','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose3','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose4','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose5','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose6','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose7','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose8','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose9','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('glucose10','food', 'juice/drink', 'refrigerated', '10', '1', now() + INTERVAL '45 days');






    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken1','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken2','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken3','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken4','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken5','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken6','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken7','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken8','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken9','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chicken10','food', 'meat/seafood', 'frozen', '10', '1', now() + '90 days');





    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk1','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk2','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk3','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk4','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk5','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk6','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk7','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk8','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk9','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('milk10','food', 'dairy/eggs', 'refrigerated', '10', '1', now() + '3 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush1', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush2', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush3', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush4', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush5', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush6', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush7', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush8', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush9', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('toothbrush10', 'supplies', 'personal_hygiene', 'drygood', '5', '1', '9999/01/01');



    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts1', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts2', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts3', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts4', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts5', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts6', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts7', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts8', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts9', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('shirts10', 'supplies', 'clothing', 'drygood', '5', '1', '9999/01/01');





    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('carrot', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('beetroot', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('radish', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('onion', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('potato', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('ginger', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('rutabaga', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('salsify', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('celery', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('breadroot', 'food', 'vegetables', 'refrigerated', '10', '2', now() + INTERVAL '10 days');




    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat1', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat2', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat3', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat4', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat5', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat6', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat7', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat8', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat9', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('wheat10', 'food', 'nuts/grains/beans', 'drygood', '10', '2', now() + INTERVAL '90 days');




    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('chilli sauce', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Dijon mustard', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Mayonnaise', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Tomato Ketchup', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Pesto genovese', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Caucasian cuisine', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Salsa', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Sriracha', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Wasabi', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('Worcestershire sauce', 'food', 'sauce/condiments/seasoning', 'drygood', '10', '2', now() + INTERVAL '90 days');




    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('orange juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('apple juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');
    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sapota juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');
    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('watermelon juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('muskmelon juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('custurd apple juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('strawberry juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blueberry juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('papaya juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('grape juice', 'food', 'juice/drink', 'refrigerated', '10', '2', now() + INTERVAL '30 days');






    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish1', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish2', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish3', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish4', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish5', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish6', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish7', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish8', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish9', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('fish10', 'food', 'meat/seafood', 'frozen', '10', '2', now() + INTERVAL '90 days');







    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs1', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs2', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs3', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');
    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs4', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs5', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs6', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs7', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs8', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs9', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('eggs10', 'food', 'dairy/eggs', 'refrigerated', '10', '2', now() + INTERVAL '20 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets1', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets2', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets3', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');
    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets4', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');
    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets5', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets6', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets7', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets8', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets9', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('blankets10', 'supplies', 'shelter', 'drygood', '5', '2', '9999/01/01');




    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries1', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries2', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries3', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries4', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries5', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries6', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries7', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries8', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries9', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('batteries10', 'supplies', 'other', 'drygood', '5', '2', '9999/01/01');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sausage1', 'food', 'meat/seafood', 'refrigerated', '5', '3', now() - INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sausage2', 'food', 'meat/seafood', 'refrigerated', '5', '3', now() - INTERVAL '10 days');



    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sausage3', 'food', 'meat/seafood', 'refrigerated', '5', '3', now() - INTERVAL '10 days');




    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sausage4', 'food', 'meat/seafood', 'refrigerated', '5', '3', now() - INTERVAL '10 days');



    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('sausage5', 'food', 'meat/seafood', 'refrigerated', '5', '3', now() - INTERVAL '10 days');



    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('cheese1', 'food', 'dairy/eggs', 'refrigerated', '5', '3', now() - INTERVAL '10 days');

    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('cheese2', 'food', 'dairy/eggs', 'refrigerated', '5', '3', now() - INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('cheese3', 'food', 'dairy/eggs', 'refrigerated', '5', '3', now() - INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('cheese4', 'food', 'dairy/eggs', 'refrigerated', '5', '3', now() - INTERVAL '10 days');


    INSERT INTO item(name, category, subcategory, storagetype, numunits, siteid, expiry) 
    VALUES('cheese5', 'food', 'dairy/eggs', 'refrigerated', '5', '3', now() - INTERVAL '10 days');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (82, 'closed', 10,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (83, 'closed', 5,  'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (162, 'closed', 5, 'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (163, 'closed', 5,  'emp1'); 



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (162, 'closed', 5,  'emp2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (163, 'closed', 5,  'emp2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (12, 'closed', 5,  'emp2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (5, 'closed', 5,  'emp2');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (132, 'closed', 5,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (133, 'closed', 5,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (14, 'closed', 5,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (4, 'closed', 5,  'emp3');




















    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (95, 'closed', 10, 'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (88, 'closed', 5,  'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (166, 'closed', 5, 'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (167, 'closed', 5,  'vol1'); 



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (165, 'closed', 5, 'vol2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (166, 'closed', 5,  'vol2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (25, 'closed', 5, 'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (30, 'closed', 5,  'vol3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (141, 'closed', 5,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (107, 'closed', 5,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (25, 'closed', 5,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (50, 'closed', 5, 'vol3');







    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (106, 'pending', 3,  'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (107, 'pending', 5,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (108, 'pending', 12,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (109, 'pending', 50,  'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (110, 'pending', 10,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (111, 'pending', 5,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (112, 'pending', 6,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (113, 'pending', 7,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (114, 'pending', 8,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'emp1');



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (168, 'pending', 4,  'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (161, 'pending', 4,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (170, 'pending', 10,  'emp1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (164, 'pending', 4,  'emp1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (165, 'pending', 4,  'emp2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (167, 'pending', 4,  'emp2');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (163, 'pending', 10,  'emp2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (169, 'pending', 4,  'emp2');



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (126, 'pending', 4,  'emp3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (127, 'pending', 4,  'emp3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (89, 'pending', 40,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (150, 'pending', 4,  'emp3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (156, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (31, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (41, 'pending', 20,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (51, 'pending', 35,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (61, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (71, 'pending', 4,  'emp3');






    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (74, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (75, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (76, 'pending', 30,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (77, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (66, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (67, 'pending', 4,  'emp3');



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (141, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (142, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (143, 'pending', 40,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (151, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (155, 'pending', 4,  'emp3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (156, 'pending', 30,  'emp3');







    /*  */


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (106, 'pending', 3,  'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (107, 'pending', 5,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (108, 'pending', 12,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (109, 'pending', 50,  'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (110, 'pending', 10,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (111, 'pending', 5,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (112, 'pending', 6,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (113, 'pending', 7,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (114, 'pending', 8,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'vol1');



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (115, 'pending', 9,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (168, 'pending', 4,  'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (161, 'pending', 4,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (170, 'pending', 10,  'vol1');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (164, 'pending', 4,  'vol1');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (165, 'pending', 4,  'vol2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (167, 'pending', 4,  'vol2');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (163, 'pending', 10,  'vol2');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (169, 'pending', 4,  'vol2');



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (126, 'pending', 4,  'vol3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (127, 'pending', 4,  'vol3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (89, 'pending', 40,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (150, 'pending', 4,  'vol3');


    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (156, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (31, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (41, 'pending', 20,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (51, 'pending', 35,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (61, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (71, 'pending', 4,  'vol3');






    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (74, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (75, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (76, 'pending', 30,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (77, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (66, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (67, 'pending', 4,  'vol3');



    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (141, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (142, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (143, 'pending', 40,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (151, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (155, 'pending', 4,  'vol3');

    INSERT INTO request(itemid, reqstate, quantity,   username) 
    values (156, 'pending', 30,  'vol3');
















































