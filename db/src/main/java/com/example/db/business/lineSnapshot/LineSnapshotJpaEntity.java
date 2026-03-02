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

    @Column(name = "operation_id", nullable = false)
    private String operationId;

    @Column(name = "payload_json", nullable = false)
    private String payloadJson;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
