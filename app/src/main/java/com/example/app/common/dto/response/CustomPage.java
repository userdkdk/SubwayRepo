package com.example.app.common.dto.response;

import lombok.Getter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CustomPage<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public static <T> CustomPage<T> of(List<T> content,
                                int pageNumber,
                                int pageSize,
                                long totalElements) {
        return new CustomPage<>(content, pageNumber, pageSize, totalElements);
    }

    public <R> CustomPage<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mappedContent = this.content.stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new CustomPage<>(mappedContent, pageNumber, pageSize,
                totalElements, totalPages, last);
    }

    private CustomPage(List<T> content,
                       int pageNumber,
                       int pageSize,
                       long totalElements) {

        this.content = content;
        this.pageNumber = pageNumber+1;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.last = pageNumber >= totalPages;
    }

    private CustomPage(List<T> content, int pageNumber, int pageSize,
                       long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}
