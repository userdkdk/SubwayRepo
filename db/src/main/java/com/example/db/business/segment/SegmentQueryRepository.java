package com.example.db.business.segment;

import com.example.db.business.segment.projection.QStationSegmentLineProjection;
import com.example.db.business.segment.projection.StationSegmentLineProjection;
import com.example.db.business.station.QStationJpaEntity;
import com.example.db.common.domain.enums.StatusFilter;
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

    public List<StationSegmentLineProjection> findByStationId(Integer stationId) {
        QSegmentJpaEntity s = QSegmentJpaEntity.segmentJpaEntity;
        QStationJpaEntity beforeStation = new QStationJpaEntity("beforeStation");
        QStationJpaEntity afterStation = new QStationJpaEntity("afterStation");

        BooleanExpression stationMatch =
                s.beforeStationJpaEntity.id.eq(stationId)
                        .or(s.afterStationJpaEntity.id.eq(stationId));
        return queryFactory
                .select(new QStationSegmentLineProjection(
                        s.lineJpaEntity.id,s.id,s.activeType,
                        beforeStation.id, beforeStation.name,
                        afterStation.id, afterStation.name
                ))
                .from(s)
                .join(s.beforeStationJpaEntity, beforeStation)
                .join(s.afterStationJpaEntity, afterStation)
                .where(stationMatch)
                .orderBy(s.lineJpaEntity.id.asc(), s.id.asc())
                .fetch();
    }
}
