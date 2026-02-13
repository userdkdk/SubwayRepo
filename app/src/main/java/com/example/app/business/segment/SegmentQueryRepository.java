package com.example.app.business.segment;

import com.example.app.business.segment.projection.QStationSegmentLineIdProjection;
import com.example.app.business.segment.projection.StationSegmentLineIdProjection;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SegmentQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final SpringDataSegmentJpaRepository segmentJpaRepository;

    public List<SegmentJpaEntity> findByLineAndActiveType(Integer lineId, StatusFilter status) {
        if (status != StatusFilter.ALL) {
            return segmentJpaRepository.findByLineJpaEntity_IdAndActiveType(
                    lineId, status.toActiveType()
            );
        }
        return segmentJpaRepository.findByLineJpaEntity_Id(lineId);
    }

    public List<SegmentJpaEntity> findAllActive() {
        return segmentJpaRepository.findByActiveType(ActiveType.ACTIVE);
    }

    public List<StationSegmentLineIdProjection> findByStationId(Integer stationId) {
        QSegmentJpaEntity s = QSegmentJpaEntity.segmentJpaEntity;

        BooleanExpression stationMatch =
                s.beforeStationJpaEntity.id.eq(stationId)
                        .or(s.afterStationJpaEntity.id.eq(stationId));
        return queryFactory
                .select(new QStationSegmentLineIdProjection(
                        s.lineJpaEntity.id,s.id,s.activeType
                ))
                .from(s)
                .where(stationMatch)
                .orderBy(s.lineJpaEntity.id.asc(), s.id.asc())
                .fetch();
    }
}
