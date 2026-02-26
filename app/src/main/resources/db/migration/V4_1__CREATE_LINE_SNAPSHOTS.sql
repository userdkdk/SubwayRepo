create table `line_snapshots` (
    id bigint primary key auto_increment,
    line_id bigint not null,
    operation_id char(32) not null,
    payload_json json not null,
    created_at datetime(6) not null,

    key idx_line_snapshots_line_id (line_id),
    key idx_line_snapshots_operation_id (operation_id),
    constraint fk_line_snapshots_line foreign key (line_id) references `lines` (id)
) engine = innodb;