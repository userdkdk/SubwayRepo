package com.example.core.business.station;

public interface StationRepository {
    boolean existsByName(String name);
    Station save(Station station);
}
