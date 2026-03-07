create table `stations` (
    id bigint primary key auto_increment,
    name varchar(255) collate utf8mb4_0900_ai_ci not null,
    status enum ('ACTIVE', 'INACTIVE') not null default 'ACTIVE',
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    unique key uk_stations_name (name)
) engine = innodb;

create table `lines` (
    id bigint primary key auto_increment,
    name varchar(255) collate utf8mb4_0900_ai_ci not null,
    status enum ('ACTIVE', 'INACTIVE') not null default 'ACTIVE',
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    unique key uk_lines_name (name)
) engine = innodb;

create table `line_stations` (
    id bigint primary key auto_increment,
    line_id bigint not null,
    station_id bigint not null,
    seq int not null,
    status enum ('ACTIVE', 'INACTIVE') not null default 'ACTIVE',
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    unique key uk_ls_line_seq (line_id, seq),
    unique key uk_ls_line_station (line_id, station_id),
    constraint fk_ls_line foreign key (line_id) references `lines` (id),
    constraint fk_ls_station foreign key (station_id) references stations (id)
) engine = innodb;

create table `segments` (
    id bigint primary key auto_increment,
    line_id bigint not null,
    before_station_id bigint not null,
    after_station_id bigint not null,
    distance double not null,
    spend_time bigint not null,
    status enum ('ACTIVE', 'INACTIVE') not null default 'ACTIVE',
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_segments_line foreign key (line_id) references `lines` (id),
    constraint fk_segments_before_station foreign key (before_station_id) references stations (id),
    constraint fk_segments_after_station foreign key (after_station_id) references stations (id)
) engine = innodb;

create table `segment_histories` (
    id bigint primary key auto_increment,
    segment_id bigint not null,
    action enum ('CREATE', 'UPDATE', 'REACTIVATE', 'DEACTIVATE') not null,
    changed_at datetime(6) not null,
    constraint fk_sh_segments foreign key (segment_id) references segments (id)
) engine = innodb;