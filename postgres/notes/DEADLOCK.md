begin transaction;


create table seats(id int primary key, isbooked bool not null, name text);
insert into seats(id, isbooked) VALUES (13, false);

#Client 1
begin transaction;


#Acquire Exclusive Lock
select * from seats where id =13 for update;