ALTER TABLE `line_snapshots`
drop column payload_json;

ALTER TABLE `line_snapshots`
drop key idx_line_snapshots_operation_id;

ALTER TABLE `line_snapshots`
drop column operation_id;

create table `line_snapshots_segments` (
    snapshot_id BIGINT NOT NULL,
    segment_id BIGINT NOT NULL,
    PRIMARY KEY (snapshot_id, segment_id),
    INDEX idx_segment_id (segment_id),
    CONSTRAINT fk_snapshot
        FOREIGN KEY (snapshot_id) REFERENCES `line_snapshots` (id)
        ON DELETE CASCADE
) engine = innodb;
