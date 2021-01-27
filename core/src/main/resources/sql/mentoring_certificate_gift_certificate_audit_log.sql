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

