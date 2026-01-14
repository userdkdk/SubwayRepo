package com.example.core.business.line;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import com.example.core.common.util.NameNormalizer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Line {

    private final Integer id;
    private final String name;
    private final ActiveType activeType;

    private static final NameNormalizer NAME_NORMALIZER = NameNormalizer.of(2, 20);

    public static Line create(String name) {
        String normalized = validateName(name);
        return new Line(null, name, ActiveType.ACTIVE);
    }

    private static String validateName(String name) {
        String normalized = NameNormalizer.normalizeName(name);
        if (!NAME_NORMALIZER.validate(normalized)) {
            throw CustomException.domain(DomainErrorCode.STATION_NAME_ERROR)
                    .addParam("name invalid",normalized);
        }
        return normalized;
    }
}
