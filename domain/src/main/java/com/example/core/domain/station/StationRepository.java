package com.example.core.domain.station;

import com.example.core.common.domain.enums.ActiveType;

import java.util.List;
import java.util.function.Consumer;

public interface StationRepository {
    void save(Station station);
    Station findByIdForUpdate(Integer id);
    List<Station> findByIdsForUpdate(List<Integer> ids);
    void updateName(Integer id, StationName name);
    void updateStatus(Integer id, ActiveType activeType);
    boolean existsActiveById(Integer id);
}
