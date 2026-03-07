package com.example.core.domain.station;

public record StationConnectionInfo (
        Integer beforeStationId,
        Integer afterStationId,
        Double beforeDistance,
        Integer beforeSpendTime,
        Double afterDistance,
        Integer afterSpendTime
){
    public StationRoleInLine role() {
        return StationRoleInLine.from(beforeStationId != null, afterStationId != null);
    }
}
