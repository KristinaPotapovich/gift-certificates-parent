create table tag
(
    id_tag bigint auto_increment
        primary key,
    name   varchar(50) not null,
    constraint tag_name_uindex
        unique (name)
);