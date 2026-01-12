package com.example.core.domain.line;

import com.example.core.domain.common.BaseEntity;
import com.example.core.domain.common.enums.ActiveType;
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
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private ActiveType activeType;
}
