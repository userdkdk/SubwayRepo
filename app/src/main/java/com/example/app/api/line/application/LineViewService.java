package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.api.station.adapter.StationApiMapper;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.app.business.line.LineQueryRepository;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.response.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineViewService {

    private final LineQueryRepository lineQueryRepository;
    private final SegmentQueryRepository segmentQueryRepository;
    private final StationSorter stationSorter;
    private final StationApiMapper stationApiMapper;

    public List<StationSegmentResponse> getStationsById(Integer lineId, StatusFilter status) {

        if (!lineQueryRepository.existsActiveById(lineId)) {
            throw CustomException.domain(AppErrorCode.LINE_NOT_FOUND)
                    .addParam("line id",lineId);
        }
        List<SegmentJpaEntity> segments = segmentQueryRepository.findByLineAndActiveType(
                lineId, status);

        log.debug("Line Id: {}, size: {}", lineId, segments.size());
        return stationSorter.sortSegments(segments).stream()
                .map(stationApiMapper::segmentEntityToDto)
                .toList();

    }

    // return line by activeType
    public List<LineResponse> getAllLines(StatusFilter status) {
        return null;
    }
}
