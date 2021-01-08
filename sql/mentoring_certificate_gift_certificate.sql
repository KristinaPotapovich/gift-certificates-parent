create table gift_certificate
(
    id_certificate   bigint auto_increment
        primary key,
    name             varchar(50)                         null,
    description      varchar(250)                        null,
    price            double                              null,
    duration         int                                 null,
    create_date      timestamp default CURRENT_TIMESTAMP not null,
    last_update_date timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);
