package com.example.core.business.station;

import com.example.core.common.domain.enums.ActiveType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Station {

    private final Integer id;
    private StationName name;
    private ActiveType activeType;

    public static Station create(StationName name) {
        return new Station(null, name, ActiveType.ACTIVE);
    }

    public static Station of(Integer id, StationName name, ActiveType activeType) {
        return new Station(id, name, activeType);
    }

    public String getName() {
        return this.name.value();
    }

    public void changeName(StationName name) {
        this.name = name;
    }

    public void changeActiveType(ActiveType activeType) {
        this.activeType = activeType;
    }
}
