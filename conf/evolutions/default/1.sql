-- Users schema

-- !Ups

CREATE TABLE USER (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE BIKE (
    serial_number varchar(255) NOT NULL,
    PRIMARY KEY (serial_number)
);

CREATE TABLE RESERVED_RENT_TOKEN (
    id varchar(255) NOT NULL,
    owner_id bigint(20) NOT NULL,
    expiration timestamp NOT NULL,
    PRIMARY KEY (id)
);

-- !Downs

DROP TABLE USER;
DROP TABLE BIKE;
DROP TABLE RESERVED_RENT_TOKEN;


