package com.example.app.business.station.api;

import com.example.app.business.station.application.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    //
//    @GetMapping("")

}
