package com.example.app.common.redis.port;

public interface RedisSegmentPort {
    String getPath();
    void setPath(String json);
    void evictPath();
}
