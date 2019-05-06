--liquibase formatted sql
--changeset kevin.raddatz:v0.0.1

create table user
(
    id                   varchar(64)  primary key not null,
    name                 varchar(256) null,
    email                varchar(256) null unique,
    email_verified       boolean      null,
    master_password_hash varchar(88) null,
    master_password_hint varchar(256) null,
    master_password_salt varchar(88) null,
    culture              char(8)      null,
    creation_date        datetime     null,
    email_token          varchar(64)  null
);

create table role (
  id   varchar(64) primary key not null,
  name varchar(255)
);

create table user_role (
  id      varchar(64) primary key not null,
  user_id varchar(64),
  role_id varchar(64),
  constraint user_id_id_fkey foreign key (user_id) references user (id),
  constraint role_id_id_fkey foreign key (role_id) references role (id)
);

INSERT INTO user (id, name, master_password_hash) VALUES
('ef0a220f-2ee7-4a31-8642-eb0db38257f0', 'admin', '$2a$10$mdrAnl3/7I9pk5ajHNgIUuaSEDVLA5DoliMZMtmnQ/z5FlHNGnOdu');
INSERT INTO role (id, name) VALUES
('cac64683-7659-4d72-9e4d-05ee13266a34', 'ADMIN');
INSERT INTO user_role (id,user_id,role_id) VALUES
('40917b5f-eb1d-4f18-8409-9c3ea4316e82', 'ef0a220f-2ee7-4a31-8642-eb0db38257f0', 'cac64683-7659-4d72-9e4d-05ee13266a34');