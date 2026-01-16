package com.example.core.business.station;

import java.util.List;
import java.util.Optional;

public interface StationRepository {
    Optional<Station> findByName(String name);
    Station upsertActivateByName(String name);
    Optional<Integer> findIdByName(String startStation);
    void inActivateByName(String name);
}
