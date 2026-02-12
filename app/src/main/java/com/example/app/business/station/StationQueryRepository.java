package com.example.app.business.station;

import com.example.app.business.station.projection.QStationProjection;
import com.example.app.business.station.projection.StationProjection;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final SpringDataStationJpaRepository stationJpaRepository;

    public List<StationJpaEntity> findByActiveType(StatusFilter status) {
        if (status != StatusFilter.ALL) {
            return stationJpaRepository.findByActiveType(status.toActiveType());
        }
        return stationJpaRepository.findAll();
    }

    public StationProjection findById(Integer stationId) {
        QStationJpaEntity h = QStationJpaEntity.stationJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(h.id.eq(stationId));

        return queryFactory
                .select(new QStationProjection(
                        h.id, h.name, h.activeType
                ))
                .from(h)
                .where(builder)
                .fetchOne();
    }
}
