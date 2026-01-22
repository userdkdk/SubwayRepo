package com.example.app.business.line;

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
@Table(name = "lines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_lines_name",
                        columnNames = "name")
        })
public class LineJpaEntity extends BaseEntity {
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

    private LineJpaEntity(String name, ActiveType activeType) {
        this.name = name;
        this.activeType = activeType;
    }

    public static LineJpaEntity create(String name) {
        return new LineJpaEntity(name, ActiveType.ACTIVE);
    }

    public void activate() {
        this.activeType = ActiveType.ACTIVE;
    }

}
