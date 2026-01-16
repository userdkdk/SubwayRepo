package com.example.app.business.station.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StationQueryRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;

}
