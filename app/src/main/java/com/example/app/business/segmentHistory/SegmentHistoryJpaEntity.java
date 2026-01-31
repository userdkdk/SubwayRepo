package com.example.app.business.segmentHistory;

import com.example.core.business.segmentHistory.HistoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "segment_histories")
public class SegmentHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "segment_id", nullable = false)
    private Integer segmentId;

    @Enumerated(EnumType.STRING)
    @Column(name="action", nullable = false)
    private HistoryType historyType;

    @Column(name="changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @PrePersist
    void onCreate() {
        this.changedAt = LocalDateTime.now();
    }

    private SegmentHistoryJpaEntity(Integer segmentId, HistoryType historyType) {
        this.segmentId = segmentId;
        this.historyType = historyType;
    }

    public static SegmentHistoryJpaEntity from(Integer segmentId, HistoryType historyType) {
        return new SegmentHistoryJpaEntity(segmentId, historyType);
    }
}
