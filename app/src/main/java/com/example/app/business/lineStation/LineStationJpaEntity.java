package com.example.app.business.lineStation;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.domain.BaseEntity;
import com.example.core.business.line.Line;
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
                @UniqueConstraint(name = "uk_ls_line_station",
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;

    private LineStationJpaEntity(LineJpaEntity line, StationJpaEntity station, ActiveType activeType) {
        this.lineJpaEntity = line;
        this.stationJpaEntity = station;
        this.activeType = activeType;
    }

    public static LineStationJpaEntity create(LineJpaEntity line, StationJpaEntity station) {
        return new LineStationJpaEntity(line, station, ActiveType.ACTIVE);
    }
}
