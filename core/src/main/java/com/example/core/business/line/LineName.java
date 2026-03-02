package com.example.core.business.line;

import com.example.core.common.util.NameValidator;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;

public record LineName (String value) {
    private static final NameValidator NAME_VALIDATOR = NameValidator.of(2, 15);

    public LineName {
        String normalized = NameValidator.normalizeName(value);
        if (!NAME_VALIDATOR.isValidate(normalized)) {
            throw CustomException.domain(DomainErrorCode.NAME_ERROR)
                    .addParam("line name invalid",normalized);
        }
        value = normalized;
    }
}
