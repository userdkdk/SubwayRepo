package com.example.app.api.station.application;

import com.example.app.api.segment.port.query.SegmentQueryPort;
import com.example.app.api.station.api.dto.response.StationDetailResponse;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.api.station.port.query.StationQueryPort;
import com.example.app.api.station.port.query.row.StationRow;
import com.example.app.api.station.port.query.row.StationSegmentRow;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.CustomException;
import com.example.core.domain.station.StationName;
import com.example.app.common.dto.page.CustomPage;
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

    private final StationQueryPort stationQueryPort;
    private final SegmentQueryPort segmentQueryPort;

    public CustomPage<StationResponse> getStations(ViewRequestFilter request) {
        PageResult<StationRow> result = stationQueryPort.findByFilter(
                request.status(), request.toPageable(), request.sortType().toDomain(), request.direction());
        List<StationResponse> content = result.content().stream()
                .map(StationResponse::from)
                .toList();
        return CustomPage.of(content, request.toPageable().getPageNumber(), request.toPageable().getPageSize(), result.totalElements());
    }

    public StationDetailResponse getStationById(Integer stationId) {
        StationRow stationProjection = stationQueryPort.findById(stationId);
        if (stationProjection == null) {
            throw CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", stationId);
        }
        List<StationSegmentRow> lineItem = segmentQueryPort.findByStationId(stationProjection.id());

        return lineItemToDetailResponse(stationId, lineItem, stationProjection);
    }

    public StationDetailResponse getStationByName(String name) {
        StationName stationName = new StationName(name);
        StationRow stationRow = stationQueryPort.findByName(stationName);
        if (stationRow == null) {
            throw CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("name", name);
        }
        List<StationSegmentRow> lineItem = segmentQueryPort.findByStationId(stationRow.id());

        return lineItemToDetailResponse(stationRow.id(), lineItem, stationRow);
    }

    private StationDetailResponse lineItemToDetailResponse(Integer stationId, List<StationSegmentRow> lineItem, StationRow station) {
        Map<Integer, List<StationDetailResponse.SegmentItem>> map = lineItem.stream()
                .collect(Collectors.groupingBy(
                        StationSegmentRow::lineId,
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
