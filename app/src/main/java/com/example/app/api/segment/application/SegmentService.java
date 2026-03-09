package com.example.app.api.segment.application;

import com.example.app.api.segment.api.dto.request.UpdateSegmentAttributeRequest;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.core.domain.segment.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SegmentService {

    private final SegmentRepository segmentRepository;

    @Transactional
    public void updateSegmentAttribute(Integer id, UpdateSegmentAttributeRequest request) {
        SegmentAttribute attribute = new SegmentAttribute(
                request.attribute().distance(), request.attribute().spendTime());
        segmentRepository.updateAttribute(id, attribute);
    }

}
