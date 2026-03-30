package com.example.app.api.path.application;

import com.example.app.api.path.api.dto.response.PathResponse;
import com.example.app.api.path.api.enums.PathFilter;
import com.example.app.api.path.port.cache.GraphCachePort;
import com.example.app.api.segment.port.query.SegmentQueryPort;
import com.example.core.domain.path.port.data.PathSegmentData;
import com.example.core.domain.path.PathCalculator;
import com.example.core.domain.path.Path;
import com.example.core.domain.path.PathResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {

    private final SegmentQueryPort segmentQueryPort;
    private final GraphCachePort graphCachePort;

    public PathResponse getPath(Integer startId, Integer endId, PathFilter pathFilter) {
        Optional<Path> cached = graphCachePort.get();
        Path path;
        log.info("isCache"+cached.isPresent());
        if (cached.isPresent()) {
            path = cached.get();
        } else {
            List<PathSegmentData> segments = segmentQueryPort.findAllActive();
            path = Path.from(segments);
            graphCachePort.set(path);
        }

        // calculate
        if (pathFilter==PathFilter.TIME) {
            PathResult res = PathCalculator.findByTime(path, startId, endId);
            return PathResponse.from(res);
        }

        PathResult res = PathCalculator.findByTime(path, startId, endId);
        return PathResponse.from(res);
    }
}
