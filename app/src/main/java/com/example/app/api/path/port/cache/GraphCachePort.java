package com.example.app.api.path.port.cache;

import com.example.core.domain.path.Path;

import java.util.Optional;

public interface GraphCachePort {
    Optional<Path> get();
    void set(Path path);
    void evict();
}
