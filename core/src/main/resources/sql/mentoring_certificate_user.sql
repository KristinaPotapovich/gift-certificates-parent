create table user
(
    id_user  bigint auto_increment
        primary key,
    login    varchar(20)                     not null,
    password varchar(20)                     not null,
    role     enum ('USER', 'ADMIN') not null,
    constraint login
        unique (login)
);
