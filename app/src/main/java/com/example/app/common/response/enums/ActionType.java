package com.example.app.common.response.enums;

import com.example.core.common.domain.enums.ActiveType;

public enum ActionType {
    ACTIVE, INACTIVE;

    public ActiveType toActiveType() {
        return ActiveType.valueOf(this.name());}
}
