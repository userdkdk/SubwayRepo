package com.example.db.business.lineSnapshot.snapthotSegment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataLineSnapshotSegmentJpaRepository extends JpaRepository<LineSnapshotSegmentJpaEntity, Integer> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            insert into line_snapshots_segments (snapshot_id, segment_id)
            select :snapshotId, s.id
            from segments s
            where s.id in (:segmentIds)
            """, nativeQuery = true)
    int insertAll(@Param("snapshotId") Integer snapshotId,
                  @Param("segmentIds") List<Integer> segmentIds);

    // test
    int countByIdSnapshotId(@Param("snapshotId") Integer snapshotId);
}
