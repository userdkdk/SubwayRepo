package com.example.app.api.line.port;

import com.example.app.api.line.port.row.LineProjection;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.core.query.CoreSortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface LineQueryPort {
    PageResult<LineProjection> findByFilter(StatusFilter status, Pageable pageable, CoreSortType sortType, Sort.Direction direction);
    boolean existsActiveById(Integer id);
}
