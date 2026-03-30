package com.example.app.api.line.port.query;

import com.example.app.api.line.port.query.row.LineProjection;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.core.query.CoreSortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface LineQueryPort {
    PageResult<LineProjection> findByFilter(StatusFilter status, Pageable pageable, CoreSortType sortType, Sort.Direction direction);
    Optional<LineProjection> findById(Integer id);
}
