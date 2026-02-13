package com.example.app.business.segmentHistory;

import com.example.app.business.segmentHistory.projection.QSegmentHistoryProjection;
import com.example.app.business.segmentHistory.projection.SegmentHistoryProjection;
import com.example.app.common.dto.response.CustomPage;
import com.example.core.business.segmentHistory.HistoryType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SegmentHistoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public CustomPage<SegmentHistoryProjection> findHistory(
            Integer segmentId,
            List<HistoryType> actions,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable) {
        QSegmentHistoryJpaEntity h = QSegmentHistoryJpaEntity.segmentHistoryJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();
        if (segmentId != null) {
            builder.and(h.segmentId.eq(segmentId));
        }
        if (actions != null && !actions.isEmpty()) {
            builder.and(h.historyType.in(actions));
        }
        if (from != null) {
            builder.and(h.changedAt.goe(from));
        }
        if (to != null) {
            builder.and(h.changedAt.lt(to));
        }
        List<SegmentHistoryProjection> list = queryFactory
                .select(new QSegmentHistoryProjection(
                        h.id, h.segmentId, h.historyType, h.changedAt))
                .from(h)
                .where(builder)
                .orderBy(h.changedAt.desc(), h.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(h.count())
                .from(h)
                .where(builder)
                .fetchOne();
        long totalCount = total==null ? 0:total;
        return CustomPage.of(list, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
    }
}
