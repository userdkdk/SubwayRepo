package com.example.core.business.station;

import com.example.core.common.util.NameValidator;
import com.example.core.exception.CustomException;
import com.example.core.exception.DomainErrorCode;

public record StationName (String value){
    private static final NameValidator NAME_VALIDATOR = NameValidator.of(2, 20);

    public StationName {
        String normalized = NameValidator.normalizeName(value);
        if (!NAME_VALIDATOR.isValidate(normalized)) {
            throw CustomException.domain(DomainErrorCode.STATION_NAME_ERROR)
                    .addParam("name invalid",normalized);
        }
        value = normalized;
    }
}
