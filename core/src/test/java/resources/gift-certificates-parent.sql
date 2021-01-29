create schema if not exists `test_db`;
USE `test_db` ;

create table gift_certificate
(
    id_certificate   bigint auto_increment            primary key,
    name             varchar(50)                         not null,
    description      varchar(250)                        not null,
    price            double                              not null,
    duration         int                                 not null,
    create_date      timestamp                           null,
    last_update_date timestamp                           null
);
create table tag
(
    id_tag bigint auto_increment
        primary key,
    name   varchar(50) not null,
    constraint tag_name_uindex
        unique (name)
);
create table certificates_tags
(
    id             bigint auto_increment
        primary key,
    id_certificate bigint not null,
    id_tag         bigint not null,
    constraint certificates_tags_ibfk_1
        foreign key (id_certificate) references gift_certificate (id_certificate),
    constraint certificates_tags_ibfk_2
        foreign key (id_tag) references tag (id_tag)
);

create index id_certificate
    on certificates_tags (id_certificate);

create index id_tag
    on certificates_tags (id_tag);

    create table order_table
(
    id_order         bigint auto_increment
        primary key,
    price            decimal   not null,
    time_of_purchase timestamp not null,
    id_user          bigint    null,
    constraint order_table_user_id_user_fk
        foreign key (id_user) references user (id_user)
);
create table orders_certificates
(
    id_order       bigint not null,
    id_certificate bigint not null,
    constraint orders_certificates_gift_certificate_id_certificate_fk
        foreign key (id_certificate) references gift_certificate (id_certificate)
            on update cascade on delete cascade,
    constraint orders_certificates_order_table_id_order_fk
        foreign key (id_order) references order_table (id_order)
            on update cascade on delete cascade
);
create table user
(
    id_user  bigint auto_increment
        primary key,
    login    varchar(20)                     not null,
    password varchar(20)                     not null,
    role     enum ('USER', 'ADMIN', 'GUEST') not null,
    constraint login
        unique (login)
);
