package com.example.db.business.lineSnapshot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataLineSnapshotJpaRepository extends JpaRepository<LineSnapshotJpaEntity, Integer> {

    @Query(value = """
            select lss.segment_id
            from line_snapshots_segments lss
            join (
                select id
                from line_snapshots
                where line_id = :line_id
                order by id desc
                limit 1
            ) latest on latest.id = lss.snapshot_id
            """, nativeQuery = true)
    List<Integer> findSegsIdByLine(@Param("line_id") Integer line_id);
}
