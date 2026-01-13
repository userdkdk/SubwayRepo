package com.example.core.business.line;

import com.example.core.common.domain.enums.ActiveType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Line {

    private final Integer id;
    private final String name;
    private final ActiveType activeType;

    public static Line create(String name) {
        return new Line(null, name, ActiveType.ACTIVE);
    }
}
