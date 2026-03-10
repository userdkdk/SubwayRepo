package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.response.StationDetailResponse;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.CustomException;
import com.example.core.domain.station.StationName;
import com.example.db.business.segment.SegmentQueryRepository;
import com.example.db.business.segment.projection.StationSegmentLineProjection;
import com.example.db.business.station.StationQueryRepository;
import com.example.db.business.station.projection.StationProjection;
import com.example.db.common.domain.PageResult;
import com.example.app.common.dto.response.CustomPage;
import lombok.RequiredArgsConstructor;
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

    public CustomPage<StationResponse> getStations(ViewRequestFilter request) {
        PageResult<StationProjection> result = stationQueryRepository.findByFilter(
                request.status(), request.toPageable(), request.sortType().toDomain(), request.direction());
        List<StationResponse> content = result.content().stream()
                .map(StationResponse::from)
                .toList();
        return CustomPage.of(content, request.toPageable().getPageNumber(), request.toPageable().getPageSize(), result.totalElements());
    }

    public StationDetailResponse getStationById(Integer stationId) {
        StationProjection stationProjection = stationQueryRepository.findById(stationId);
        if (stationProjection == null) {
            throw CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", stationId);
        }
        List<StationSegmentLineProjection> lineItem = segmentQueryRepository.findByStationId(stationProjection.id());

        return lineItemToDetailResponse(stationId, lineItem, stationProjection);
    }

    public StationDetailResponse getStationByName(String name) {
        StationName stationName = new StationName(name);
        StationProjection stationProjection = stationQueryRepository.findByName(stationName);
        if (stationProjection == null) {
            throw CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("name", name);
        }
        List<StationSegmentLineProjection> lineItem = segmentQueryRepository.findByStationId(stationProjection.id());

        return lineItemToDetailResponse(stationProjection.id(), lineItem, stationProjection);
    }

    private StationDetailResponse lineItemToDetailResponse(Integer stationId, List<StationSegmentLineProjection> lineItem, StationProjection station) {
        Map<Integer, List<StationDetailResponse.SegmentItem>> map = lineItem.stream()
                .collect(Collectors.groupingBy(
                        StationSegmentLineProjection::lineId,
                        Collectors.mapping(
                                r-> {
                                    boolean currentIsBefore = r.beforeStationId().equals(stationId);

                                    return new StationDetailResponse.SegmentItem(
                                            r.segmentId(),
                                            r.activeType(),
                                            currentIsBefore ? r.afterStationId() : r.beforeStationId(),
                                            currentIsBefore ? r.afterStationName() : r.beforeStationName(),
                                            currentIsBefore ? StationDetailResponse.SegmentPosition.NEXT :
                                                    StationDetailResponse.SegmentPosition.PREV
                                    );
                                }, Collectors.toList()
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
