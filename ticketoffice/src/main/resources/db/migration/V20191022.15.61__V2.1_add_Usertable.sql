DROP TABLE IF EXISTS ticket_do;
DROP TABLE IF EXISTS customer_do;

create table ticket_do (
                           tid bigint not null,
                           date varchar(255),
                           price float not null,
                           state varchar(255),
                           valid_from varchar(255),
                           valid_to varchar(255),
                           version bigint not null,
                           customer_cid bigint,
                           primary key (tid)
);

create table customer_do (
                             cid bigint not null,
                             address varchar(255),
                             name varchar(255),
                             version bigint not null,
                             primary key (cid)
);


alter table ticket_do
    add constraint FK38tqdinm1e9g779k156q1eb2
        foreign key (customer_cid)
            references customer_do;