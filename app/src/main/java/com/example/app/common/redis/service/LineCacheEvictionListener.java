package com.example.app.common.redis.service;

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
        }
    }
}
