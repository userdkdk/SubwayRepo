alter table `line_stations` drop column seq;

alter table `segments`
add unique key uk_segments_line_bs_as (line_id, before_station_id, after_station_id);