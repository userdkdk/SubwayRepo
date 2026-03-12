package com.example.core.domain.station;

import com.example.core.common.domain.enums.ActiveType;

import java.util.List;

public interface StationRepository {
    void save(Station station);
    Station findByIdForUpdate(Integer id);
    void findAllByIdsForUpdate(List<Integer> ids);
    void updateName(Integer id, StationName name);
    void updateStatus(Integer id, ActiveType activeType);
    boolean existsActiveById(Integer id);
}
