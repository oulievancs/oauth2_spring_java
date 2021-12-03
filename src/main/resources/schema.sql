/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  e.oulis
 * Created: Nov 15, 2021
 */

create table users(
    username varchar2(100) not null primary key,
    password varchar2(100) not null,
    enabled number(1) not null
);

create table authorities (
    username varchar2(50) not null,
    authority varchar2(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);

create table groups (
    id number(5) primary key,
    group_name varchar2(50) not null
);

create table group_authorities (
    group_id number(5) not null,
    authority varchar2(50) not null,
    constraint fk_group_authorities_group foreign key(group_id) references groups(id)
);

create table group_members (
    id number(5) primary key,
    username varchar2(50) not null,
    group_id number(5) not null,
    constraint fk_group_members_group foreign key(group_id) references groups(id)
);

create sequence groups_seq
minvalue 0
maxvalue 99999
start with 0
increment by 1
cache 20;

create sequence group_members_seq
minvalue 0
maxvalue 99999
start with 0
increment by 1
cache 20;


create or replace trigger groups_trg
  before insert on groups  
  for each row
declare
begin
  select groups_seq.nextval into :NEW.id from dual;
end;
--end groups_trg;

create or replace trigger group_members_trg
  before insert on group_members  
  for each row
declare
begin
  select group_members_seq.nextval into :NEW.id from dual;
end;
--end group_members_trg;

-- simple tablespace of 10mb
-- create tablespace SPRING_SEC_OAUTH2_DATA
--      datafile '/opt/sfw/oracle/SPRING_SEC_OAUTH2_DATA/SPRING_SEC_OAUTH2_DATA.dbf' size 10M
--      extent management local autoallocate segment space management auto;
-- 
-- -- create the user schema for spring sec oauth 2
-- create user SPRING_SEC_OAUTH2 identified by "SPRING_SEC_OAUTH2_PW" default tablespace SPRING_SEC_OAUTH2_DATA;
-- grant connect to SPRING_SEC_OAUTH2;
-- alter user SPRING_SEC_OAUTH2 quota unlimited on SPRING_SEC_OAUTH2_DATA;
-- grant all privileges to SPRING_SEC_OAUTH2;

-- This is the schema structure for v 1.0.0.M6d
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  autoapprove VARCHAR(255),
  additional_information VARCHAR(2048)
  --additional_information VARCHAR(4096)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication BLOB,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication BLOB
);

create table oauth_code (
  code VARCHAR(256), authentication BLOB
);