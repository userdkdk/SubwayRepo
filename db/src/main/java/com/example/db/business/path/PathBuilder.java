package com.example.db.business.path;

import com.example.core.domain.path.port.data.PathSegmentData;
import com.example.core.domain.path.Path;
import com.example.core.domain.path.PathEdge;
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
        List<PathSegmentData> segments = segmentQueryRepository.findAllActive();
        Map<Integer, List<PathEdge>> adj = new HashMap<>();

        for (PathSegmentData s : segments) {
            int from = s.beforeStationId();
            int to = s.afterStationId();

            adj.computeIfAbsent(from, k->new ArrayList<>())
                    .add(new PathEdge(
                            to,
                            s.distance(),
                            s.spendTime()
                    ));

            adj.computeIfAbsent(to,k->new ArrayList<>())
                    .add(new PathEdge(
                            from,
                            s.distance(),
                            s.spendTime()
                    ));
        }
        return new Path(adj);
    }
}
