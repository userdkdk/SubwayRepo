package com.example.app.api.path.api.dto.response;

import com.example.app.business.path.model.PathResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PathResponse {
    private Boolean found;
    private Integer startId;
    private Integer endId;
    private List<Integer> stations;
    private Integer totalTimeSec;
    private Integer totalDistance;

    public static PathResponse from(PathResult pathResult) {
        return PathResponse.builder()
                .found(pathResult.found())
                .startId(pathResult.startId())
                .endId(pathResult.endId())
                .stations(List.copyOf(pathResult.stations()))
                .totalTimeSec(pathResult.totalTimeSec())
                .totalDistance(pathResult.totalDistance())
                .build();
    }
}
