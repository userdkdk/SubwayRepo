package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.domain.BaseEntity;
import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "segments",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SEGMENTS_LINE_BS_AS",
                        columnNames = {"line_id", "before_station_id", "after_station_id"})
        })
public class SegmentJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private LineJpaEntity lineJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "before_station_id", nullable = false)
    private StationJpaEntity beforeStationJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_station_id", nullable = false)
    private StationJpaEntity afterStationJpaEntity;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "spend_time", nullable = false)
    private Integer spendTime;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;

    private SegmentJpaEntity(LineJpaEntity lineRef, StationJpaEntity beforeRef, StationJpaEntity afterRef,
                             double distance, int spendTime, ActiveType activeType) {
        this.lineJpaEntity = lineRef;
        this.beforeStationJpaEntity = beforeRef;
        this.afterStationJpaEntity = afterRef;
        this.distance = distance;
        this.spendTime = spendTime;
        this.activeType = activeType;
    }

    public static SegmentJpaEntity create(LineJpaEntity lineRef, StationJpaEntity beforeRef, StationJpaEntity afterRef,
                                          double distance, int spendTime) {
        return new SegmentJpaEntity(lineRef, beforeRef, afterRef,
                distance, spendTime, ActiveType.ACTIVE);
    }

    public void setAttribute(Double distance, Integer spendTime) {
        this.distance = distance;
        this.spendTime = spendTime;
    }

    public boolean isActive() {
        return this.activeType==ActiveType.ACTIVE;
    }
}
