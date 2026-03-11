package com.example.core.domain.segment;

import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;

public record SegmentAttribute(Double distance, Integer spendTimeSeconds) {

    public SegmentAttribute {
        if (distance == null || spendTimeSeconds == null) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_ATTRIBUTE_VALUE_ERROR,
                    "distance or spendTime is null");
        }
        if (distance <= 0 || distance >= 1000000) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_ATTRIBUTE_VALUE_ERROR)
                    .addParam("distance", distance);
        }
        if (spendTimeSeconds <= 0 || spendTimeSeconds >= 1000000) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_ATTRIBUTE_VALUE_ERROR)
                    .addParam("spend time", spendTimeSeconds);
        }
    }
}
