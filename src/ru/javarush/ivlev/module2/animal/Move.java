package ru.javarush.ivlev.module2.animal;

import ru.javarush.ivlev.module2.island.Direction;

public interface Move {
    void move(Direction direction, int distance);
}
