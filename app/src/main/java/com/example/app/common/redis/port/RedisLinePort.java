package com.example.app.common.redis.port;

import com.example.app.common.dto.request.enums.StatusFilter;

public interface RedisLinePort {
    String getSegments(Integer lineId, StatusFilter status);
    void setSegments(Integer lineId, StatusFilter status, String json);
    void evictSegments(Integer lineId);
}
