package com.example.core.business.station;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import com.example.core.common.util.NameValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Station {

    private final Integer id;
    private String name;
    private ActiveType activeType;

    private static final NameValidator NAME_VALIDATOR = NameValidator.of(2, 20);

    public static Station create(String name) {
        String normalized = validateName(name);
        return new Station(null, normalized, ActiveType.ACTIVE);
    }

    public static Station of(Integer id, String name, ActiveType activeType) {
        return new Station(id, name, activeType);
    }

    public void changeName(String name) {
        this.name = validateName(name);
    }

    public void changeActiveType(ActiveType activeType) {
        this.activeType = activeType;
    }

    private static String validateName(String name) {
        String normalized = NameValidator.normalizeName(name);
        if (!NAME_VALIDATOR.isValidate(normalized)) {
            throw CustomException.domain(DomainErrorCode.STATION_NAME_ERROR)
                    .addParam("name invalid",normalized);
        }
        return normalized;
    }

}
