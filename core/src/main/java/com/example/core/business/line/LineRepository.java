package com.example.core.business.line;

import java.util.function.Consumer;

public interface LineRepository{
    Line save(Line line);
    void updateAttribute(Integer id, LineName name);
    void deActiveLine(Integer id);
    void activeLine(Integer id);
    boolean existsActiveById(Integer id);
}
