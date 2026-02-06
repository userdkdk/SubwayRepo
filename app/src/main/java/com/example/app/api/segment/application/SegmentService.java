package com.example.app.api.segment.application;

import com.example.app.api.segment.api.dto.request.UpdateSegmentAttributeRequest;
import com.example.app.api.segment.api.dto.request.UpdateSegmentStatusRequest;
import com.example.app.common.response.enums.ActionType;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.business.segment.SegmentRepository;
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
        SegmentAttribute attribute = request.attribute();
        segmentRepository.update(id, segment -> segment.changeSegmentAttribute(attribute));
    }

    @Transactional
    public void updateSegmentStatus(Integer id, UpdateSegmentStatusRequest request) {
        ActionType action = request.actionType();
        //
        segmentRepository.update(id, segment -> {
            // before, after
        });
    }
}
