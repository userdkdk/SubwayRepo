package com.example.core.business.station;

import java.util.List;
import java.util.Optional;

public interface StationRepository {
    // save or update status active
    Station upsertActivateByName(String name);
    Optional<Integer> findIdByName(String startStation);
    Optional<Station> findByName(String name);
    Station save(Station station);
}
