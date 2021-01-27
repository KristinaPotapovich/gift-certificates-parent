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
