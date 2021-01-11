Playing around with postgres performance

docker run --name pgmain -d -e POSTGRES_PASSWORD=postgres postgres

create table grades_original(id serial not null, grade int not null);

insert into grades_original(grade) select floor(random()*100) from generate_series(0,10000000);

create index grades_org_index on grades_original(grade);

\d -> describe table


explain analyze select count(*) from grades_original where grade = 30;

create table grades_parts(id serial not null, grade int not null) partition by range(grade);

create table g0035 (like grades_parts including indexes);
create table g3560 (like grades_parts including indexes);
create table g6080 (like grades_parts including indexes);
create table g80100 (like grades_parts including indexes);

alter table grades_parts attach partition g0035 for values from (0) to (35); 
alter table grades_parts attach partition g3560 for values from (35) to (60);
alter table grades_parts attach partition g6080 for values from (60) to (80); 
alter table grades_parts attach partition g80100 for values from (80) to (100); 


# DATABASE WILL CHOOSE WHICH PARTITION BASED ON THE VALUE OF GRADE ##
insert into grades_parts select * from grades_org;

# CREATE INDEX ON ALL PARTITIONS ## 
create index grades_parts_index on grades_parts(grade);

explain analyze select count(*) from grades_parts where grade =30;


## RELATION SIZE SHOW HOW LARGE THE RELATIONS ARE ## 
select pg_relation_size(oid), relname from pg_class order by pg_relation_size(oid) desc;


## ENABLE PARTITION PRUNING ##
show ENABLE_PARTITION_PRUNING;