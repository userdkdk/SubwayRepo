package com.example.app.business.station;

import com.example.app.business.station.projection.QStationProjection;
import com.example.app.business.station.projection.StationProjection;
import com.example.app.common.dto.request.enums.SortType;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.app.common.dto.response.CustomPage;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public CustomPage<StationProjection> findByActiveType(StatusFilter status, Pageable pageable, SortType sortType, Sort.Direction direction) {
        QStationJpaEntity s = QStationJpaEntity.stationJpaEntity;

        BooleanExpression statusMatch =
                (status == null || status==StatusFilter.ALL) ?
                null : s.activeType.eq(status.toActiveType());

        OrderSpecifier<?> orderSpecifier = orderBy(s, sortType, direction);

        List<StationProjection> content = queryFactory
                .select(new QStationProjection(
                        s.id, s.name, s.activeType
                ))
                .from(s)
                .where(statusMatch)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(s.count())
                .from(s)
                .where(statusMatch)
                .fetchOne();
        long totalCount = total==null ? 0:total;

        return CustomPage.of(content, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
    }

    public StationProjection findById(Integer stationId) {
        QStationJpaEntity s = QStationJpaEntity.stationJpaEntity;

        return queryFactory
                .select(new QStationProjection(
                        s.id, s.name, s.activeType
                ))
                .from(s)
                .where(s.id.eq(stationId))
                .fetchOne();
    }

    private OrderSpecifier<?> orderBy(QStationJpaEntity s, SortType sortType, Sort.Direction direction) {
        Order order = direction.isAscending() ? Order.ASC : Order.DESC;

        return switch (sortType) {
            case NAME -> new OrderSpecifier<>(order, s.name);
            case ID -> new OrderSpecifier<>(order, s.id);
        };
    }
}
