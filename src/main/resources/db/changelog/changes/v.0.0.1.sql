--liquibase formatted sql
--changeset kevin.raddatz:v0.0.1

create table user
(
    id                   varchar(64) primary key not null,
    name                 varchar(256)            not null,
    email                varchar(256)            not null unique,
    email_verified       boolean                 not null,
    master_password_hash varchar(88)             not null,
    master_password_hint varchar(256)            null,
    culture              varchar(8)              null,
    creation_date        datetime                not null,
    email_token          varchar(64)             not null,
    public_key           longtext                not null
);

create table role
(
    id   varchar(64) primary key not null,
    name varchar(255)
);

create table user_role
(
    id      varchar(64) primary key not null,
    user_id varchar(64),
    role_id varchar(64),
    constraint fk_user_role_user_id foreign key (user_id) references user (id),
    constraint fk_user_role_role_id foreign key (role_id) references role (id)
);

create table secret
(
    id            varchar(64)  not null primary key,
    title         varchar(256) not null,
    secret_id     varchar(64)  not null,
    version       int          not null,
    user_id       varchar(64)  not null,
    creation_date datetime     not null,
    data          longtext     not null,
    type          int          not null,
    constraint fk_secret_user_id foreign key (user_id) references user (id)
);
