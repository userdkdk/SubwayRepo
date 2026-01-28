package com.example.core.business.segment;

import java.util.function.Consumer;

public interface SegmentRepository{
    void save(Segment segment);
    void update(Integer id, Consumer<Segment> updater);
}
