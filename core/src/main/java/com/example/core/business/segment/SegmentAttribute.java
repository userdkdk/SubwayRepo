package com.example.core.business.segment;

import com.example.core.exception.CustomException;
import com.example.core.exception.DomainErrorCode;

public record SegmentAttribute(Double distance, Integer spendTimeSeconds) {

    public SegmentAttribute {
        if (distance == null || spendTimeSeconds == null) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                    "distance or spendTime is null");
        }
        if (distance <= 0) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_INPUT_VALUE_ERROR)
                    .addParam("distance", distance);
        }
        if (spendTimeSeconds <= 0) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_INPUT_VALUE_ERROR)
                    .addParam("spend time", spendTimeSeconds);
        }
    }
}
