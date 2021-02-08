create table orders_certificates_audit_log
(
    REV            int     not null,
    id_order       bigint  not null,
    id_certificate bigint  not null,
    REVTYPE        tinyint null,
    primary key (REV, id_order, id_certificate)
)
    engine = MyISAM;

