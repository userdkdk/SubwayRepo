package com.example.app.api.station.port;

import com.example.app.api.station.port.row.StationRow;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.core.domain.station.StationName;
import com.example.core.query.CoreSortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface StationQueryPort {
    PageResult<StationRow>  findByFilter(StatusFilter status, Pageable pageable, CoreSortType sortType, Sort.Direction direction);
    StationRow findById(Integer stationId);
    StationRow findByName(StationName stationName);
}
