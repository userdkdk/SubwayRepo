package com.example.db.business.line;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.query.CoreSortType;
import com.example.db.business.line.projection.LineProjection;
import com.example.db.business.line.projection.QLineProjection;
import com.example.db.business.station.projection.QStationProjection;
import com.example.db.business.station.projection.StationProjection;
import com.example.db.common.domain.PageResult;
import com.example.db.common.domain.enums.StatusFilter;
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
public class LineQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PageResult<LineProjection> findByFilter(StatusFilter status, Pageable pageable, CoreSortType sortType, Sort.Direction direction) {
        QLineJpaEntity l = QLineJpaEntity.lineJpaEntity;

        BooleanExpression statusMatch =
                (status == null || status==StatusFilter.ALL) ?
                        null : l.activeType.eq(status.toActiveType());

        OrderSpecifier<?> orderSpecifier = orderBy(l, sortType, direction);

        List<LineProjection> content = queryFactory
                .select(new QLineProjection(
                        l.id, l.name, l.activeType
                ))
                .from(l)
                .where(statusMatch)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(l.count())
                .from(l)
                .where(statusMatch)
                .fetchOne();
        long totalCount = total==null ? 0:total;

        return new PageResult<>(content, totalCount);
    }

    public boolean existsActiveById(Integer id) {
        return false;
//        return lineJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }


    private OrderSpecifier<?> orderBy(QLineJpaEntity l, CoreSortType sortType, Sort.Direction direction) {
        Order order = direction.isAscending() ? Order.ASC : Order.DESC;

        return switch (sortType) {
            case CoreSortType.NAME -> new OrderSpecifier<>(order, l.name);
            case CoreSortType.ID -> new OrderSpecifier<>(order, l.id);
        };
    }
}
