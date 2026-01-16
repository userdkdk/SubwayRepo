package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.domain.BaseEntity;
import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "segments")
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
    private double distance;

    @Column(name = "spend_time", nullable = false)
    private int spendTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;
}
