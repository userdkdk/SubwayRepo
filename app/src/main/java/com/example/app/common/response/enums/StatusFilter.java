package com.example.app.common.response.enums;

import com.example.core.common.domain.enums.ActiveType;

public enum StatusFilter {
    ACTIVE, INACTIVE, ALL;

    public ActiveType toActiveType() {
        return ActiveType.valueOf(this.name());
    }
}
