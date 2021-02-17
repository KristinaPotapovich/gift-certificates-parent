
create table gift_certificate
(
    id_certificate   bigint auto_increment            primary key,
    name             varchar(50)                         not null,
    description      varchar(250)                        not null,
    price            double                              not null,
    duration         int                                 not null,
    create_date      timestamp                           null,
    last_update_date timestamp                           null,
    is_deleted       bit                                 not null
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

    create table gift_certificate_audit_log
(
    id_certificate   bigint         not null,
    REV              int            not null,
    REVTYPE          tinyint        null,
    create_date      datetime       null,
    description      varchar(255)   null,
    duration         int            null,
    last_update_date datetime       null,
    name             varchar(255)   null,
    price            decimal(19, 2) null,
    primary key (id_certificate, REV)
)
    engine = MyISAM;

create index FKhf7uuci7j7of1xcb7lxgehor4
    on gift_certificate_audit_log (REV);

    create table tag_audit_log
(
    id_tag  bigint       not null,
    REV     int          not null,
    REVTYPE tinyint      null,
    name    varchar(255) null,
    primary key (id_tag, REV)
)
    engine = MyISAM;


create table certificates_tags_audit_log
(
    REV            int     not null,
    id_certificate bigint  not null,
    id_tag         bigint  not null,
    REVTYPE        tinyint null,
    primary key (REV, id_certificate, id_tag)
)
    engine = MyISAM;


    create table user_audit_log
(
    id_user  bigint       not null,
    REV      int          not null,
    REVTYPE  tinyint      null,
    login    varchar(255) null,
    password varchar(255) null,
    role     varchar(255) null,
    primary key (id_user, REV)
)
    engine = MyISAM;


create index FKi5b5ivrfvc0pgts5ngcs0g5i3
    on user_audit_log (REV);

    create table order_table_audit_log
(
    id_order         bigint         not null,
    REV              int            not null,
    REVTYPE          tinyint        null,
    price            decimal(19, 2) null,
    time_of_purchase datetime       null,
    id_user          bigint         null,
    primary key (id_order, REV)
)
    engine = MyISAM;

create index FKgnavn8y59n6a1a2hltdvu0qt5
    on order_table_audit_log (REV);

    create table orders_certificates_audit_log
(
    REV            int     not null,
    id_order       bigint  not null,
    id_certificate bigint  not null,
    REVTYPE        tinyint null,
    primary key (REV, id_order, id_certificate)
)
    engine = MyISAM;


create index FK89024hk7yjnfm6monvx71tboj
    on tag_audit_log (REV);

