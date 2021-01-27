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
