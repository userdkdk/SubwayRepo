package com.example.app.api.history.api.dto.item;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.segmentHistory.HistoryType;
import com.example.core.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record SegmentHistorySearchCondition(
        @Schema(description = "조회할 액션 타입들. 미지정 시 전체", example = "CREATE")
        List<HistoryType> actions,

        @Schema(description = "조회 시작 시각(ISO-8601). 미지정 시 to 기준 7일 전")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime from,

        @Schema(description = "조회 종료 시각(ISO-8601). 미지정 시 현재 시각")
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
