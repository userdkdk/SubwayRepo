package com.example.app.business.line;

import com.example.app.common.domain.BaseEntity;
import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_lines_name",
                        columnNames = "name")
        })
public class LineJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;

    @Column(name = "counts", nullable = false)
    private int counts;

    private LineJpaEntity(String name, ActiveType activeType, int counts) {
        this.name = name;
        this.activeType = activeType;
        this.counts = counts;
    }

    public static LineJpaEntity create(String name, ActiveType activeType, int counts) {
        return new LineJpaEntity(name, activeType, counts);
    }

    public void activate() {
        this.activeType = ActiveType.ACTIVE;
    }
}
