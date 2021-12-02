insert into badminton_organizations (name) values ('organization aa')
update badminton_organizations SET id  = 1 where name = 'organization aa'
insert into badminton_users (name, surname, email,password,organization_id) values ('James', 'Doe', 'aa.bb@ss.com','pass',1)
update badminton_users SET id = 1 where name = 'James'
