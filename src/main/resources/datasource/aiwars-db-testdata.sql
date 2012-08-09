insert into users(username, password, enabled) values ('admin','admin',true);
insert into users(username, password, enabled) values ('bbogovich','password',true);
insert into users(username, password, enabled) values ('user1','password',true);
insert into users(username, password, enabled) values ('user2','password',true);
insert into users(username, password, enabled) values ('user3','password',true);
insert into users(username, password, enabled) values ('user4','password',true);

insert into authorities(username,authority) values ('admin','ROLE_ADMIN');
insert into authorities(username,authority) values ('admin','ROLE_USER');
insert into authorities(username,authority) values ('user1','ROLE_USER');
insert into authorities(username,authority) values ('user2','ROLE_USER');
insert into authorities(username,authority) values ('user3','ROLE_USER');
insert into authorities(username,authority) values ('user4','ROLE_USER');

insert into users(username, password, enabled) values ('guest','guest',true);
insert into authorities(username,authority) values ('guest','ROLE_USER');

commit;