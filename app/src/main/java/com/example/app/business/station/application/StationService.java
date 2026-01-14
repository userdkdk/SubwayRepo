package com.example.app.business.station.application;

import com.example.core.business.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;


}
