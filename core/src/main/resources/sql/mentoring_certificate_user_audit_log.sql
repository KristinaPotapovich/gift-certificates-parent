create table user_audit_log
(
    id_user  bigint       not null,
    REV      int          not null,
    REVTYPE  tinyint      null,
    login    varchar(255) null,
    password varchar(255) null,
    role     varchar(255) null,
    primary key (id_user, REV)
)
    engine = MyISAM;

create index FKi5b5ivrfvc0pgts5ngcs0g5i3
    on user_audit_log (REV);

