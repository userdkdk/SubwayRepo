package com.example.app.common.dto.request.enums;

import com.example.core.query.CoreSortType;

public enum SortType {
    ID, NAME;

    public CoreSortType toDomain() {
        return CoreSortType.valueOf(this.name());
    }
}
