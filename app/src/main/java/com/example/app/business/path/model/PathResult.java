package com.example.app.business.path.model;

import java.util.List;

public record PathResult(
        boolean found,
        int startId,
        int endId,
        List<Integer> stations,
        int totalTimeSec,
        int totalDistance
){
    public static PathResult single(int stationId) {
        return new PathResult(true, stationId, stationId,
                List.of(stationId),0,0);
    }

    public static PathResult found(int startId, int endId, List<Integer> stations,
                                   int totalTimeSec, int totalDistance) {
        return new PathResult(true, startId, endId,
                stations, totalTimeSec, totalDistance);
    }

    public static PathResult notFound(int startId, int endId) {
        return new PathResult(false, startId, endId,
                List.of(), 0, 0);
    }
}
