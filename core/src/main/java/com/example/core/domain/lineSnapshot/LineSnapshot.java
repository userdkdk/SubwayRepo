package com.example.core.domain.lineSnapshot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineSnapshot {

    private final Integer id;
    private final Integer lineId;

    public static LineSnapshot create(Integer lineId) {
        return new LineSnapshot(null, lineId);
    }
}
