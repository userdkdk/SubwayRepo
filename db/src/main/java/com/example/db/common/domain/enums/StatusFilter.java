package com.example.db.common.domain.enums;

import com.example.core.common.domain.enums.ActiveType;

public enum StatusFilter {
    ACTIVE, INACTIVE, ALL;

    public ActiveType toActiveType() {
        return ActiveType.valueOf(this.name());
    }
}
