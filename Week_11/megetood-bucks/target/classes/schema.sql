drop table t_menu if exists;
drop table t_order if exists;
drop table t_order_coffee if exists;

create table T_COFFEE(
    id bigint not null auto_increment,
    name varchar(255),
    price bigint not null,
    create_time timestamp ,
    update_time timestamp ,
    primary key (id)
);

create table t_order(
    id bigint not null auto_increment,
    customer varchar(255),
    state integer not null,
    create_time timestamp ,
    update_time timestamp ,
    primary key (id)
);

create table t_order_coffee(
    coffee_order_id bigint not null,
    items_id bigint not null
);

insert into T_COFFEE(name,price,create_time,update_time) values('espresso','2000',now(),now());
insert into T_COFFEE(name,price,create_time,update_time) values('latte','3200',now(),now());
insert into T_COFFEE(name,price,create_time,update_time) values('capuccino','1900',now(),now());
insert into T_COFFEE(name,price,create_time,update_time) values('mocha','2800',now(),now());
insert into T_COFFEE(name,price,create_time,update_time) values('macchiato','5000',now(),now());
