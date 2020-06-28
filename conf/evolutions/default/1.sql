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

-- !Downs

DROP TABLE USER;
DROP TABLE BIKE;


