create table certificates_tags_audit_log
(
    REV            int     not null,
    id_certificate bigint  not null,
    id_tag         bigint  not null,
    REVTYPE        tinyint null,
    primary key (REV, id_certificate, id_tag)
)
    engine = MyISAM;

