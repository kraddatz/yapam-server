--liquibase formatted sql
--changeset kevin.raddatz:v1

create table user
(
    id            varchar(64) primary key not null,
    name          varchar(256)            not null,
    email         varchar(256)            not null unique,
    locale        varchar(8)              null,
    creation_date datetime                not null,
    public_key    longtext                not null
);

create table secret
(
    id            varchar(64)  not null primary key,
    title         varchar(256) not null,
    secret_id     varchar(64)  not null,
    version       int          not null,
    creation_date datetime     not null,
    data          longtext     not null,
    type          int          not null,
    created_by    varchar(64)  not null,
    constraint fk_secret_created_by foreign key (created_by) references user (id),
    unique (secret_id, version)
);

create table user_secret
(
    user_id    varchar(64) not null,
    secret_id  varchar(64) not null,
    privileged tinyint(1)  not null,
    constraint fk_user_secret_user foreign key (user_id) references user (id),
    constraint fk_user_secret_secret foreign key (secret_id) references secret (id),
    primary key (user_id, secret_id)
);

create table file
(
    id            varchar(64)  not null primary key,
    filename      varchar(255) not null,
    filesize      bigint       not null,
    hash          varchar(44)  not null,
    mimetype      varchar(16)  not null,
    creation_date datetime     not null,
    created_by    varchar(64)  not null,
    constraint fk_file_created_by foreign key (created_by) references user (id)
);

create table secret_file
(
    secret_id varchar(64) not null,
    file_id   varchar(64) not null,
    primary key (secret_id, file_id),
    constraint fk_secret_file_secret_id foreign key (secret_id) references secret (id),
    constraint fk_secret_file_file_id foreign key (file_id) references file (id)
);

create table tag
(
    id            varchar(64)  not null primary key,
    name          varchar(255) not null unique,
    creation_date datetime     not null,
    created_by    varchar(64)  not null,
    constraint fk_tag_created_by foreign key (created_by) references user (id)
);

create table secret_tag
(
    secret_id varchar(64) not null,
    tag_id    varchar(64) not null,
    primary key (secret_id, tag_id),
    constraint fk_secret_tag_secret_id foreign key (secret_id) references secret (id),
    constraint fk_secret_tag_tag_id foreign key (tag_id) references tag (id)
);
