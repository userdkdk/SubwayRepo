package com.example.core.domain.station;

import com.example.core.domain.common.BaseEntity;
import com.example.core.domain.common.enums.ActiveType;
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
public class Station extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private ActiveType activeType;
}
