package com.example.core.business.line;

import java.util.function.Consumer;

public interface LineRepository{
    Line save(Line line);
    void ensureExistsForUpdate(Integer id);
    void updateAttribute(Integer id, LineName name);
    void activeLine(Integer id);
    void deactiveLine(Integer id);
    boolean existsActiveById(Integer id);
}
