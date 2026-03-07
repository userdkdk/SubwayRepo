package com.example.db.business.path;

import com.example.core.domain.path.Path;
import com.example.core.domain.path.PathEdge;
import com.example.db.business.segment.SegmentJpaEntity;
import com.example.db.business.segment.SegmentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PathBuilder {

    private final SegmentQueryRepository segmentQueryRepository;

    public Path build() {
        List<SegmentJpaEntity> segments = segmentQueryRepository.findAllActive();
        Map<Integer, List<PathEdge>> adj = new HashMap<>();

        for (SegmentJpaEntity s : segments) {
            int from = s.getBeforeStationJpaEntity().getId();
            int to = s.getAfterStationJpaEntity().getId();

            adj.computeIfAbsent(from, k->new ArrayList<>())
                    .add(new PathEdge(
                            to,
                            s.getDistance(),
                            s.getSpendTime()
                    ));

            adj.computeIfAbsent(to,k->new ArrayList<>())
                    .add(new PathEdge(
                            from,
                            s.getDistance(),
                            s.getSpendTime()
                    ));
        }
        return new Path(adj);
    }
}
