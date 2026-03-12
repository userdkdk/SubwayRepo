package com.example.core.domain.line;

import com.example.core.common.domain.enums.ActiveType;

public interface LineRepository{
    Line save(Line line);
    Line ensureExistsForUpdate(Integer id);
    void updateName(Integer id, LineName name);
    void updateStatus(Integer id, ActiveType activeType);
    void activeLine(Integer id);
    void deactiveLine(Integer id);
    boolean existsActiveById(Integer id);
}
