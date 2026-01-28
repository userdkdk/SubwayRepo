package com.example.core.business.segment;

import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;

public record SegmentAttribute(double distance, int spendTimeSeconds) {

    public SegmentAttribute {
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
