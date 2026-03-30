package com.example.app.api.line.port.cache;

import com.example.app.common.dto.request.enums.StatusFilter;

import java.util.Optional;

public interface LineDetailCachePort {
    Optional<LineDetailCacheValue> get(Integer lineId, StatusFilter status);
    void set(Integer lineId, StatusFilter status, LineDetailCacheValue value);
    void evict(Integer lineId);
}
