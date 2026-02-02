package com.example.core.business.station;

import java.util.function.Consumer;

public interface StationRepository {
    Station save(Station station);
    void update(Integer id, Consumer<Station> updater);
    boolean existsActiveById(Integer id);
}
