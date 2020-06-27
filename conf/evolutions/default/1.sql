-- Users schema

-- !Ups

CREATE TABLE USER (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

-- !Downs

DROP TABLE USER;


