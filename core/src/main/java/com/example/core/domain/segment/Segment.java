package com.example.core.domain.segment;

import com.example.core.domain.common.BaseEntity;
import com.example.core.domain.common.enums.ActiveType;
import com.example.core.domain.line.Line;
import com.example.core.domain.station.Station;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "segments")
public class Segment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "before_station_id", nullable = false)
    private Station beforeStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_station_id", nullable = false)
    private Station afterStation;

    @Column(name = "distance", nullable = false)
    private double distance;

    @Column(name = "spend_time", nullable = false)
    private int spendTime;

    @Column(name = "status", nullable = false)
    private ActiveType activeType;
}
