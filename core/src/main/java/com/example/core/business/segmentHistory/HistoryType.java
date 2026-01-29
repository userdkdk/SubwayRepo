package com.example.core.business.segmentHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum HistoryType {
    CREATE,
    UPDATE,
    REACTIVATE,
    DEACTIVATE;
}
