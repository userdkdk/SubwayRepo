package com.example.core.business.line;

public interface LineRepository{
    boolean existsByName(String name);

    void save(Line line);

    Line findByName(String name);

    void active(Line line);
}
