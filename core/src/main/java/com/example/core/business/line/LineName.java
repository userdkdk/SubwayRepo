package com.example.core.business.line;

import com.example.core.common.util.NameValidator;
import com.example.core.exception.CustomException;
import com.example.core.exception.DomainErrorCode;

public record LineName (String value) {
    private static final NameValidator NAME_VALIDATOR = NameValidator.of(2, 20);

    public LineName {
        String normalized = NameValidator.normalizeName(value);
        if (!NAME_VALIDATOR.isValidate(normalized)) {
            throw CustomException.domain(DomainErrorCode.LINE_NAME_ERROR)
                    .addParam("name invalid",normalized);
        }
        value = normalized;
    }
}
