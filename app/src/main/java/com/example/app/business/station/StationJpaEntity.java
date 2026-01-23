package com.example.app.business.station;

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
@Table(name = "stations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_stations_name",
            columnNames = "name")
    })
public class StationJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;

    private StationJpaEntity(String name, ActiveType activeType) {
        this.name = name;
        this.activeType = activeType;
    }

    public static StationJpaEntity create(String name, ActiveType activeType) {
        return new StationJpaEntity(name, activeType);
    }

    public void activate() {
        this.activeType = ActiveType.ACTIVE;
    }
}
