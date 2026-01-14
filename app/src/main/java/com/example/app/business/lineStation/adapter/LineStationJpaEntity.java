package com.example.app.business.lineStation.adapter;

import com.example.app.business.line.adapter.LineJpaEntity;
import com.example.app.business.station.adapter.StationJpaEntity;
import com.example.app.common.domain.BaseEntity;
import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "line_stations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_line_seq",
                        columnNames = {"line_id", "seq"}),
                @UniqueConstraint(name = "uk_line_station",
                        columnNames = {"line_id", "station_id"})
        })
public class LineStationJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private LineJpaEntity lineJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private StationJpaEntity stationJpaEntity;

    @Column(name = "seq", nullable = false)
    private int seq;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;

}
