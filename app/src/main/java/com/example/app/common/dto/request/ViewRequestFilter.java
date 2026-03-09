package com.example.app.common.dto.request;

import com.example.app.common.dto.request.enums.SortType;
import com.example.db.common.domain.enums.StatusFilter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record ViewRequestFilter (
        StatusFilter status,
        SortType sortType,
        Sort.Direction direction,
        @Min(0) Integer page,
        @Min(1) @Max(100) Integer size
){
    public ViewRequestFilter {
        status = status == null ? StatusFilter.ACTIVE : status;
        sortType = sortType == null ? SortType.ID : sortType;
        direction = direction == null ? Sort.Direction.ASC : direction;
        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
