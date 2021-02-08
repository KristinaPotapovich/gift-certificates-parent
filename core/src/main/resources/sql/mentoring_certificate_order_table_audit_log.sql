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

