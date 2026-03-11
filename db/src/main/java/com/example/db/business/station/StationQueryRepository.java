package com.example.db.business.station;

import com.example.app.api.station.port.StationQueryPort;
import com.example.core.domain.station.StationName;
import com.example.core.query.CoreSortType;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.app.api.station.port.row.StationRow;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
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
public class StationQueryRepository implements StationQueryPort {
    private final JPAQueryFactory queryFactory;

    @Override
    public PageResult<StationRow> findByFilter(StatusFilter status, Pageable pageable, CoreSortType sortType, Sort.Direction direction) {
        QStationJpaEntity s = QStationJpaEntity.stationJpaEntity;

        BooleanExpression statusMatch =
                (status == null || status==StatusFilter.ALL) ?
                null : s.activeType.eq(status.toActiveType());

        OrderSpecifier<?> orderSpecifier = orderBy(s, sortType, direction);

        List<StationRow> content = queryFactory
                .select(Projections.constructor(
                        StationRow.class,
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

        return new PageResult<>(content, totalCount);
    }

    @Override
    public StationRow findById(Integer stationId) {
        QStationJpaEntity s = QStationJpaEntity.stationJpaEntity;

        return queryFactory
                .select(Projections.constructor(
                        StationRow.class,
                        s.id, s.name, s.activeType
                ))
                .from(s)
                .where(s.id.eq(stationId))
                .fetchOne();
    }

    @Override
    public StationRow findByName(StationName stationName) {
        QStationJpaEntity s = QStationJpaEntity.stationJpaEntity;

        return queryFactory
                .select(Projections.constructor(
                        StationRow.class,
                        s.id, s.name, s.activeType
                ))
                .from(s)
                .where(s.name.eq(stationName.value()))
                .fetchOne();
    }

    private OrderSpecifier<?> orderBy(QStationJpaEntity s, CoreSortType sortType, Sort.Direction direction) {
        Order order = direction.isAscending() ? Order.ASC : Order.DESC;

        return switch (sortType) {
            case CoreSortType.NAME -> new OrderSpecifier<>(order, s.name);
            case CoreSortType.ID -> new OrderSpecifier<>(order, s.id);
        };
    }
}
