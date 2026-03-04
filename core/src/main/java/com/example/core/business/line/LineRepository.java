package com.example.core.business.line;

import com.example.core.common.domain.enums.ActiveType;

import java.util.function.Consumer;

public interface LineRepository{
    Line save(Line line);
    void ensureExistsForUpdate(Integer id);
    void updateAttribute(Integer id, LineName name);
    int updateStatus(Integer id, ActiveType from, ActiveType target);
    void activeLine(Integer id);
    void deactiveLine(Integer id);
    boolean existsActiveById(Integer id);
}
