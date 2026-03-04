package com.example.core.business.lineSnapshot;

import java.util.List;

public interface LineSnapshotRepository {
    Integer save(Integer id);

    List<Integer> findSegsIdByLine(Integer id);
}
