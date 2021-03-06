/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  e.oulis
 * Created: Nov 12, 2021
 */

--
--Users: john/john@123 kelly/kelly@123
--Password encrypted using CodeachesBCryptPasswordEncoder.java (4 rounds)
--
INSERT INTO users (username,password,enabled) 
    VALUES ('john', '$2a$04$xqJH/AWpC89pBBFb7i9VU.zoWbOrE2gvdFcfTAOE1bCF5.tNvVXXu', 1);
INSERT INTO users (username,password,enabled) 
    VALUES ('kelly','$2a$04$IpZnGqXXgNvvMbqlg/tc7uJUM.1nj/5KtqnFlxRpRN2RqWUFV4lg6', 1);
  
INSERT INTO groups (id, group_name) VALUES (1, 'INVENTORY_GROUP_1');
INSERT INTO groups (id, group_name) VALUES (2, 'INVENTORY_GROUP_2');

INSERT INTO group_authorities (group_id, authority) VALUES (1, 'INVENTORY_VIEW');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'INVENTORY_ADD');

INSERT INTO group_authorities (group_id, authority) VALUES (2, 'INVENTORY_VIEW');

INSERT INTO group_members (username, group_id) VALUES ('john', 1);
INSERT INTO group_members (username, group_id) VALUES ('kelly', 2);

--
--Client: appclient/appclient@123
--Password encrypted using CodeachesBCryptPasswordEncoder.java (4 rounds)
--
INSERT INTO
  oauth_client_details (
    client_id,
    client_secret,
    resource_ids,
    scope,
    authorized_grant_types,
    access_token_validity,
    refresh_token_validity
  )
VALUES
  (
    'appclient',
    '$2a$04$ZVENvHhtvDKPSgMsP9AK0usr9o3Dpo2G3aSAT1HQZSZUB7CoAP6QC',
    'carInventory',
    'read,write',
    'authorization_code,check_token,refresh_token,password',
    1000000,
    1000000
  );