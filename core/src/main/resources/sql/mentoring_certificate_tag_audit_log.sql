create table tag_audit_log
(
    id_tag  bigint       not null,
    REV     int          not null,
    REVTYPE tinyint      null,
    name    varchar(255) null,
    primary key (id_tag, REV)
)
    engine = MyISAM;

create index FK89024hk7yjnfm6monvx71tboj
    on tag_audit_log (REV);

