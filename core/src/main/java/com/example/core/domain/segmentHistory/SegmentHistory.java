package com.example.core.domain.segmentHistory;

import com.example.core.domain.segment.Segment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "segment_histories")
public class SegmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    @Column(name="action", nullable = false)
    private HistoryType historyType;

    @CreatedDate
    @Column(name="changed_at", nullable = false, updatable = false)
    private Instant changedAt;
}
