package com.example.db.business.segment;

import com.example.app.api.line.port.row.LineSegmentRow;
import com.example.core.domain.path.port.data.PathSegmentData;
import com.example.app.api.segment.port.SegmentQueryPort;
import com.example.app.api.station.port.row.StationSegmentRow;
import com.example.db.business.station.QStationJpaEntity;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SegmentQueryRepository implements SegmentQueryPort {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<LineSegmentRow> findByLineAndActiveType(Integer lineId, StatusFilter status) {
        QSegmentJpaEntity s = QSegmentJpaEntity.segmentJpaEntity;
        QStationJpaEntity beforeStation = new QStationJpaEntity("beforeStation");
        QStationJpaEntity afterStation = new QStationJpaEntity("afterStation");
        BooleanExpression stationMatch =
                s.lineJpaEntity.id.eq(lineId)
                        .and(s.activeType.eq(status.toActiveType()));
        return queryFactory
                .select(Projections.constructor(
                        LineSegmentRow.class,
                        s.id,
                        beforeStation.id, beforeStation.name,
                        afterStation.id, afterStation.name,
                        s.distance, s.spendTime
                ))
                .from(s)
                .join(s.beforeStationJpaEntity, beforeStation)
                .join(s.afterStationJpaEntity, afterStation)
                .where(stationMatch)
                .fetch();
    }

    @Override
    public List<PathSegmentData> findAllActive() {
        return null;
    }

    @Override
    public List<StationSegmentRow> findByStationId(Integer stationId) {
        QSegmentJpaEntity s = QSegmentJpaEntity.segmentJpaEntity;
        QStationJpaEntity beforeStation = new QStationJpaEntity("beforeStation");
        QStationJpaEntity afterStation = new QStationJpaEntity("afterStation");

        BooleanExpression stationMatch =
                s.beforeStationJpaEntity.id.eq(stationId)
                        .or(s.afterStationJpaEntity.id.eq(stationId));
        return queryFactory
                .select(Projections.constructor(
                        StationSegmentRow.class,
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
