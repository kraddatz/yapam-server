--liquibase formatted sql
--changeset kevin.raddatz:v0.0.2

create table file
(
    id        varchar(64)  not null,
    filename  varchar(255) not null,
    secret_id varchar(64)  null,
    filesize  bigint       not null,
    hash      varchar(40)  not null,
    constraint fk_file_secret_id foreign key (secret_id) references secret (id)
);
