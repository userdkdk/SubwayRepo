package com.example.app.common.cache;

import com.example.app.api.line.port.cache.LineDetailCachePort;
import com.example.app.api.path.port.cache.GraphCachePort;
import com.example.app.api.segment.port.query.SegmentQueryPort;
import com.example.app.api.station.port.query.row.StationSegmentRow;
import com.example.app.logging.event.AppLogEvent;
import com.example.app.logging.event.ErrorLogEvent;
import com.example.app.logging.logger.LogEventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheInvalidationService {
    private static final String GRAPH_CACHE_EVICT_FAIL = "Failed to evict graph cache";
    private static final String LINE_DETAIL_CACHE_EVICT_FAIL = "Failed to evict line detail cache";

    private final GraphCachePort graphCachePort;
    private final LineDetailCachePort lineDetailCachePort;
    private final SegmentQueryPort segmentQueryPort;

    public void invalidateByLineAttributeChanged(Integer lineId) {
        evictLineDetail(lineId, "LineChangedEvent");
    }

    public void invalidateByLineStatusChanged(Integer lineId) {
        evictGraph("LineStatusChangedEvent", Map.of("lineId", lineId));
        evictLineDetail(lineId, "LineStatusChangedEvent");
    }

    public void invalidateBySegmentStatusChanged(Integer lineId) {
        evictGraph("SegmentChangedEvent", Map.of("lineId", lineId));
        evictLineDetail(lineId, "SegmentStatusChangedEvent");
    }

    public void invalidateBySegmentAttributeChanged(Integer lineId) {
        evictGraph("SegmentAttributeChangedEvent", Map.of("lineId", lineId));
        evictLineDetail(lineId, "SegmentAttributeChangedEvent");
    }

    public void invalidateByStationAttributeChanged(Integer stationId) {
        List<Integer> lineIds = findRelatedLineIds(stationId);
        for (Integer lineId : lineIds) {
            evictLineDetail(lineId, "StationAttributeChangedEvent");
        }
    }

    private List<Integer> findRelatedLineIds(Integer stationId) {
        try {
            return segmentQueryPort.findByStationId(stationId).stream()
                    .map(StationSegmentRow::lineId)
                    .toList();
        } catch (Exception e) {
            cacheLog(
                    "Failed to find related lineIds for station cache invalidation",
                    Map.of("stationId", stationId),
                    e
            );
            return List.of();
        }
    }

    private void evictGraph(String eventType, Map<String, Object> baseParams) {
        try {
            graphCachePort.evict();
        } catch (Exception e) {
            Map<String, Object> params = new HashMap<>(baseParams);
            params.put("eventType", eventType);
            cacheLog(GRAPH_CACHE_EVICT_FAIL, params, e);
        }
    }

    private void evictLineDetail(Integer lineId, String eventType) {
        try {
            lineDetailCachePort.evict(lineId);
        } catch (Exception e) {
            Map<String, Object> params = new HashMap<>();
            params.put("lineId", lineId);
            params.put("eventType", eventType);
            cacheLog(LINE_DETAIL_CACHE_EVICT_FAIL, params, e);
        }
    }

    private void cacheLog(String message, Map<String, Object> params, Exception e) {
        AppLogEvent log = ErrorLogEvent.fromParams(
                message,
                params,
                e.getMessage()
        );
        LogEventLogger.log(log);
    }
}
