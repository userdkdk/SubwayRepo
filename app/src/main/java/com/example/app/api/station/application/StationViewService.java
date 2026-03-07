package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.response.StationDetailResponse;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.CustomException;
import com.example.db.business.segment.SegmentQueryRepository;
import com.example.db.business.segment.projection.StationSegmentLineIdProjection;
import com.example.db.business.station.StationQueryRepository;
import com.example.app.common.dto.request.enums.SortType;
import com.example.db.business.station.projection.StationProjection;
import com.example.db.common.domain.PageResult;
import com.example.db.common.domain.enums.StatusFilter;
import com.example.app.common.dto.response.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationViewService {

    private final StationQueryRepository stationQueryRepository;
    private final SegmentQueryRepository segmentQueryRepository;

    public CustomPage<StationResponse> getStations(StatusFilter status, Pageable pageable, SortType sortType, Sort.Direction direction) {
        PageResult<StationProjection> result = stationQueryRepository.findByActiveType(status, pageable, sortType.toType(), direction);
        List<StationResponse> content = result.content().stream()
                .map(StationResponse::from)
                .toList();
        return CustomPage.of(content, pageable.getPageNumber(), pageable.getPageSize(), result.totalElements());
    }

    public StationDetailResponse getStationById(Integer stationId) {
        StationProjection station = stationQueryRepository.findById(stationId);
        if (station == null) {
            throw CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", stationId);
        }
        List<StationSegmentLineIdProjection> lineItem = segmentQueryRepository.findByStationId(stationId);
        Map<Integer, List<StationDetailResponse.SegmentItem>> map = lineItem.stream()
                .collect(Collectors.groupingBy(
                        StationSegmentLineIdProjection::lineId,
                        Collectors.mapping(
                                r-> new StationDetailResponse.SegmentItem(r.segmentId(),r.activeType()),
                                Collectors.toList()
                        )
                ));
        return new StationDetailResponse(station.id(), station.name(), station.activeType(),
                map.entrySet().stream()
                        .map(e-> new StationDetailResponse.LineItem(
                                e.getKey(),
                                e.getValue()
                        ))
                        .toList());
    }
}
