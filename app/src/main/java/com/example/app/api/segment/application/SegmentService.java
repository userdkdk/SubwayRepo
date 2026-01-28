package com.example.app.api.segment.application;

import com.example.app.api.segment.api.dto.request.UpdateSegmentRequest;
import com.example.core.business.segment.SegmentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SegmentService {

    private final SegmentRepository segmentRepository;

    @Transactional
    public void updateSegment(Integer segmentId, UpdateSegmentRequest request) {


    }
}
