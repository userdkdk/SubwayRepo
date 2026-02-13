package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.response.StationDetailResponse;
import com.example.app.api.station.api.dto.response.StationSummaryResponse;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.business.segment.projection.StationSegmentLineIdProjection;
import com.example.app.business.station.StationQueryRepository;
import com.example.app.business.station.projection.StationProjection;
import com.example.app.common.dto.request.enums.SortType;
import com.example.app.common.dto.request.enums.StatusFilter;
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

    public CustomPage<StationSummaryResponse> getStations(StatusFilter status, Pageable pageable, SortType sortType, Sort.Direction direction) {
        CustomPage<StationProjection> rows = stationQueryRepository.findByActiveType(status, pageable, sortType, direction);
        return rows.map(StationSummaryResponse::from);
    }

    public StationDetailResponse getStationById(Integer stationId) {
        StationProjection stations = stationQueryRepository.findById(stationId);
        List<StationSegmentLineIdProjection> lineItem = segmentQueryRepository.findByStationId(stationId);
        Map<Integer, List<StationDetailResponse.SegmentItem>> map = lineItem.stream()
                .collect(Collectors.groupingBy(
                        StationSegmentLineIdProjection::lineId,
                        Collectors.mapping(
                                r-> new StationDetailResponse.SegmentItem(r.segmentId(),r.activeType()),
                                Collectors.toList()
                        )
                ));
        return new StationDetailResponse(stationId, stations.name(), stations.activeType(),
                map.entrySet().stream()
                        .map(e-> new StationDetailResponse.LineItem(
                                e.getKey(),
                                e.getValue()
                        ))
                        .toList());
    }

}
