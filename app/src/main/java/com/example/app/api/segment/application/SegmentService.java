package com.example.app.api.segment.application;

import com.example.app.api.segment.api.dto.request.UpdateSegmentRequest;
import com.example.app.common.response.enums.StatusFilter;
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
    public void updateSegment(Integer id, UpdateSegmentRequest request) {
        segmentRepository.update(id, segment -> {
            StatusFilter newStatus = request.status();
            SegmentAttribute attribute = request.attribute();
            // change status
            if (newStatus!=null && newStatus!=StatusFilter.ALL) {
                // if to active
                // if between inactive that and active...


            }
            // change attribute
        });
    }
}
