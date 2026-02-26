package com.example.core.business.lineSnapshot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineSnapshot {

    private final Integer id;
    private final Integer lineId;
    private final String operationId;
    private final String payloadJson;
}
