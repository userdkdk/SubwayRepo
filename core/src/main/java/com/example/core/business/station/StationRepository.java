package com.example.core.business.station;

import java.util.Optional;

public interface StationRepository {
    Optional<Station> findByName(String name);
    Station save(Station station);
    void inActivate(Station station);
    void activate(Station station);
}
