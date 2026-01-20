package com.example.core.business.line;

public interface LineRepository{
    Line save(Line line);
    void inActivate(Line line);
    void Activate(Line line);

}
