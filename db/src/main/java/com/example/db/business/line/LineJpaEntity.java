package com.example.db.business.line;

import com.example.db.common.domain.BaseEntity;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActiveType activeType;

    @Version
    @Column(nullable = false)
    private Long version;

    private LineJpaEntity(String name, ActiveType activeType) {
        this.name = name;
        this.activeType = activeType;
    }

    public static LineJpaEntity create(String name, ActiveType activeType) {
        return new LineJpaEntity(name, activeType);
    }

    public void changeActiveType(ActiveType activeType) {
        this.activeType = activeType;
    }

    public void changeName(String name) {
        this.name = name;
    }

}
