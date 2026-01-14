package com.example.core.business.station;

public interface StationRepository {
    boolean existsByName(String name);
    void save(Station station);
}
