package com.example.app.common.dto.request.enums;

import com.example.core.query.StationSortType;

public enum SortType {
    ID, NAME;

    public StationSortType toDomain() {
        return StationSortType.valueOf(this.name());
    }
}
