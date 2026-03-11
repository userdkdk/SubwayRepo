package com.example.app.api.history.api.dto.item;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.domain.segmentHistory.HistoryType;
import com.example.core.common.exception.CustomException;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record SegmentHistorySearchCondition(
        List<HistoryType> actions,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime from,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime to
) {
    public SegmentHistorySearchCondition {
        if (to==null) {
            to = LocalDateTime.now();
        }
        if (from==null) {
            from = to.minusDays(7);
        }
        if (actions == null) {
            actions = List.of();
        }
        if (from.isAfter(to)) {
            throw CustomException.app(AppErrorCode.HISTORY_CONDITION_INValid);
        }
    }
}
