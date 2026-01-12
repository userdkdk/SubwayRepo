package com.example.core.domain.lineStation;

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
@Table(name = "line_stations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_line_seq",
                        columnNames = {"line_id", "seq"}),
                @UniqueConstraint(name = "uk_line_station",
                        columnNames = {"line_id", "station_id"})
        })
public class LineStation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "seq", nullable = false)
    private int seq;

    @Column(name = "status", nullable = false)
    private ActiveType activeType;

}
