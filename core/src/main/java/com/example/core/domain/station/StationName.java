package com.example.core.domain.station;

import com.example.core.common.util.NameValidator;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;

public record StationName (String value){
    private static final NameValidator NAME_VALIDATOR = NameValidator.of(2, 15);

    public StationName {
        String normalized = NameValidator.normalizeName(value);
        if (!NAME_VALIDATOR.isValidate(normalized)) {
            throw CustomException.domain(DomainErrorCode.NAME_ERROR)
                    .addParam("station name invalid",normalized);
        }
        value = normalized;
    }
}
