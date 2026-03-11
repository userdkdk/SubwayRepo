package com.example.core.domain.segmentHistory;

import lombok.Getter;

@Getter
public enum HistoryType {
    CREATE,
    UPDATE,
    REACTIVATE,
    DEACTIVATE;
}
