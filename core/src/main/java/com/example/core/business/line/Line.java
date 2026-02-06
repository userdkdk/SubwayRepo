package com.example.core.business.line;

import com.example.core.common.domain.enums.ActiveType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Line {

    private final Integer id;
    private LineName name;
    private ActiveType activeType;

    public static Line create(LineName name) {
        return new Line(null, name, ActiveType.ACTIVE);
    }

    public static Line of(Integer id, LineName name, ActiveType activeType) {
        return new Line(id, name, activeType);
    }

    public void changeName(LineName name) {
        this.name = name;
    }

    public void changeActiveType(ActiveType activeType) {
        this.activeType = activeType;
    }
}
