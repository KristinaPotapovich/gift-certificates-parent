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