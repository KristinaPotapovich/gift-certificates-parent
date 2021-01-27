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

