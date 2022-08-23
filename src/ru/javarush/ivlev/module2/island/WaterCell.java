package ru.javarush.ivlev.module2.island;

import java.util.List;

public class WaterCell extends Cell{
    public WaterCell(Island parent, List<AnimalType> animaltypes, int posX, int posY) {
        super(parent, posX, posY);
    }
}
