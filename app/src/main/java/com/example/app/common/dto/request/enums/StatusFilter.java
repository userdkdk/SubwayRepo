package com.example.app.common.dto.request.enums;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;

public enum StatusFilter {
    ACTIVE, INACTIVE, ALL;

    public ActiveType toActiveType() {
        if (this==StatusFilter.ALL) {
            throw CustomException.app(AppErrorCode.VIEW_STATUS_ERROR);
        }
        return ActiveType.valueOf(this.name());
    }
}
