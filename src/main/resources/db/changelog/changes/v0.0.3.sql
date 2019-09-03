--liquibase formatted sql
--changeset kevin.raddatz:v0.0.3

create table tag
(
  id varchar(64) not null primary key,
  name varchar(255) not null
);

create table secret_tag
(
  secret_id varchar(64) not null,
  tag_id varchar(64) not null unique,
  primary key (secret_id, tag_id),
  constraint fk_secret_tag_secret_id foreign key (secret_id) references secret(id),
  constraint fk_secret_tag_tag_id foreign key (tag_id) references tag(id)
);
