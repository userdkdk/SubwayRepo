package com.example.app.common.redis.service;

import com.example.app.common.exception.AppErrorCode;
import com.example.app.logging.event.AppLogEvent;
import com.example.app.logging.event.ErrorLogEvent;
import com.example.app.logging.logger.LogEventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LineCacheEvictionListener {
    private final RedisSegmentService redisSegmentService;
    private final RedisLineService redisLineService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(LineSegmentChangedEvent event) {
        Integer lineId = event.lineId();
        try {
            redisSegmentService.evictPath();
            redisLineService.evictSegments(lineId);
        } catch (Exception e) {
            AppLogEvent log = new ErrorLogEvent(
                    "Redis line cache evict error",
                    null,
                    null,
                    AppErrorCode.INTERNAL_SERVER_ERROR,
                    null,
                    e.getMessage()
            );
            LogEventLogger.log(log);
        }
    }
}
