package com.example.db.business.lineSnapshot.snapthotSegment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataLineSnapshotSegmentJpaRepository extends JpaRepository<LineSnapshotSegmentJpaEntity, Integer> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            insert into line_snapshots_segments (snapshot_id, segment_id)
            select :snapshotId, s.id
            from segments s
            where s.line_id = :lineId
             and s.status = 'ACTIVE'
            """, nativeQuery = true)
    int insertAllAcitvateByLineId(
            @Param("snapshotId") Integer snapshotId,
            @Param("lineId") Integer lineId);


    // test
    int countByIdSnapshotId(@Param("snapshotId") Integer snapshotId);
}
