package com.example.db.business.lineSnapshot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "line_snapshots")
public class LineSnapshotJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "line_id", nullable = false)
    private Integer lineId;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    private LineSnapshotJpaEntity(Integer lineId) {
        this.lineId = lineId;
    }

    public static LineSnapshotJpaEntity create(Integer lineId) {
        return new LineSnapshotJpaEntity(lineId);
    }
}
