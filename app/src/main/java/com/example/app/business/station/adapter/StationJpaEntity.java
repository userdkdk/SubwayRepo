package com.example.app.business.station.adapter;

import com.example.app.common.domain.BaseEntity;
import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_stations_name",
            columnNames = "name")
    })
public class StationJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private ActiveType activeType;
}
