package com.example.core.business.line;

import java.util.function.Consumer;

public interface LineRepository{
    Line save(Line line);
    void update(Integer id, Consumer<Line> updater);

}
