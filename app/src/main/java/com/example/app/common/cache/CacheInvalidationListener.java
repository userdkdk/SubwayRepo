package com.example.app.common.cache;

import com.example.app.api.line.event.LineAttributeChangedEvent;
import com.example.app.api.line.event.LineStatusChangedEvent;
import com.example.app.api.segment.event.SegmentAttributeChangedEvent;
import com.example.app.api.segment.event.SegmentStatusChangedEvent;
import com.example.app.api.station.event.StationAttributeChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CacheInvalidationListener {

    private final CacheInvalidationService cacheInvalidationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(LineAttributeChangedEvent event) {
        cacheInvalidationService.invalidateByLineAttributeChanged(event.lineId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(LineStatusChangedEvent event) {
        cacheInvalidationService.invalidateByLineStatusChanged(event.lineId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(SegmentStatusChangedEvent event) {
        cacheInvalidationService.invalidateBySegmentStatusChanged(event.lineId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(SegmentAttributeChangedEvent event) {
        cacheInvalidationService.invalidateBySegmentAttributeChanged(event.lineId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(StationAttributeChangedEvent event) {
        cacheInvalidationService.invalidateByStationAttributeChanged(event.stationId());
    }
}
