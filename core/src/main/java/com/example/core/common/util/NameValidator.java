package com.example.core.common.util;

import com.example.core.exception.CustomException;
import com.example.core.exception.DomainErrorCode;

import java.util.regex.Pattern;

public class NameValidator {

    private final Pattern pattern;

    // 한글,영어 대소문자,숫자,띄어쓰기,하이픈만 허용
    private NameValidator(int min, int max) {
        this.pattern = Pattern.compile(
                "^[a-zA-Z0-9가-힣 \\-]{"+min+","+max+"}$");
    }

    public static NameValidator of (int min, int max) {
        if (min<=0 || max < min) {
            throw CustomException.domain(DomainErrorCode.NAME_POLICY_ERROR,
                    "name normalize policy error");
        }
        return new NameValidator(min, max);
    }

    public static String normalizeName(String raw) {
        if (raw == null) {
            throw CustomException.domain(DomainErrorCode.NAME_ERROR, "name is null");
        }
        String name = raw.trim();
        if (name.isBlank()) {
            throw CustomException.domain(DomainErrorCode.NAME_ERROR,"name is blank");
        }
        return name;
    }

    public boolean isValidate(String name) {
        return pattern.matcher(name).matches() &&
                !name.startsWith("-") && !name.endsWith("-");
    }
}
