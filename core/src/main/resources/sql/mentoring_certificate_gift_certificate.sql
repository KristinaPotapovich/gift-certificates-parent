create table gift_certificate
(
    id_certificate   bigint auto_increment
        primary key,
    name             varchar(50)                         not null,
    description      varchar(250)                        not null,
    price            decimal                             not null,
    duration         int                                 not null,
    create_date      timestamp default CURRENT_TIMESTAMP null,
    last_update_date timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

