package com.example.core.domain.segmentHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HistoryType {
    CREATE("Create"),
    UPDATE("Update"),
    REACTIVATE("Reactivate"),
    DEACTIVATE("Deactivate");

    private final String status;
}
