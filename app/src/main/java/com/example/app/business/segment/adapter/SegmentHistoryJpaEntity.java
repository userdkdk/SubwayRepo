package com.example.app.business.segment.adapter;

import com.example.core.business.segmentHistory.HistoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "segment_histories")
public class SegmentHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private SegmentJpaEntity segmentJpaEntity;

    @Column(name="action", nullable = false)
    private HistoryType historyType;

    @CreatedDate
    @Column(name="changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;
}
