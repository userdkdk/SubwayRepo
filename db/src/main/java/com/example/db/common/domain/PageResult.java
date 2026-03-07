package com.example.db.common.domain;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        long totalElements
) {}
