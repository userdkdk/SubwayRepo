package com.example.app.api.path.application;

import com.example.app.api.line.application.StationSorter;
import com.example.app.api.path.api.dto.response.PathResponse;
import com.example.app.api.path.api.enums.PathFilter;
import com.example.app.business.line.LineQueryRepository;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.common.redis.service.RedisLineService;
import com.example.app.common.response.enums.StatusFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {

    private final LineQueryRepository lineQueryRepository;
    private final SegmentQueryRepository segmentQueryRepository;
    private final StationSorter stationSorter;
    private final RedisLineService redisLineService;
    private final ObjectMapper redisObjectMapper;

    public PathResponse getPath(Integer lineId, Integer startId, Integer endId, PathFilter pathFilter) {
        // get all station
        //
        String cachedJson = redisLineService.getSegments(lineId, StatusFilter.ACTIVE);


        // get all stations (
        return null;
    }
}
