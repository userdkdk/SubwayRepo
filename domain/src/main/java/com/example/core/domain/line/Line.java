package com.example.core.domain.line;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
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

    public String getName() {
        return this.name.value();
    }

    public void changeName(LineName name) {
        this.name = name;
    }

    public void isActive() {
        if (this.activeType != ActiveType.ACTIVE) {
            throw CustomException.app(DomainErrorCode.LINE_STATUS_CONFLICT);
        }
    }

    public void ensureChangeActiveType(ActiveType target) {
        if (this.activeType == target) {
            throw CustomException.domain(DomainErrorCode.LINE_STATUS_CONFLICT);
        }
        this.activeType = target;
    }
}
