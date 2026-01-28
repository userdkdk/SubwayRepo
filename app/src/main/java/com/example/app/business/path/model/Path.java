package com.example.app.business.path.model;

import java.util.List;
import java.util.Map;

public record Path(Map<Integer, List<PathEdge>> adj) {

    public List<PathEdge> edgesOf(int stationId) {
        return adj.getOrDefault(stationId, List.of());
    }
}