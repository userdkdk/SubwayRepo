package com.example.core.domain.path;

import com.example.core.domain.path.port.data.PathSegmentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Path(Map<Integer, List<PathEdge>> adj) {

    public List<PathEdge> edgesOf(int stationId) {
        return adj.getOrDefault(stationId, List.of());
    }

    public static Path from(List<PathSegmentData> segments) {
        Map<Integer, List<PathEdge>> adj = new HashMap<>();

        for (PathSegmentData s : segments) {
            int from = s.beforeStationId();
            int to = s.afterStationId();

            adj.computeIfAbsent(from, k -> new ArrayList<>())
                    .add(new PathEdge(to, s.distance(), s.spendTime()));

            adj.computeIfAbsent(to, k -> new ArrayList<>())
                    .add(new PathEdge(from, s.distance(), s.spendTime()));
        }

        return new Path(adj);
    }
}