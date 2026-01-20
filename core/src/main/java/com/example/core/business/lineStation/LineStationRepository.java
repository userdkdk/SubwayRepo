package com.example.core.business.lineStation;

import java.util.Optional;

public interface LineStationRepository{
    Optional<LineStation> findByLineIdAndStationId(Integer id, Integer start);
    void save(LineStation lineStation);
}
