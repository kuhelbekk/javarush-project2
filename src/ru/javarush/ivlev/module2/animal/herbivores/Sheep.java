package ru.javarush.ivlev.module2.animal.herbivores;

import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.island.Direction;

public class Sheep extends  Herbivore{
    @Override
    public boolean eat(IslandItem item) {
        return false;
    }

    @Override
    public void move(Direction direction, int distance) {

    }
}
