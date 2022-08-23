package ru.javarush.ivlev.module2.island;

import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.plant.Plant;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class IslandCell extends Cell {

    public IslandCell(Island parent, int posX, int posY) {
        super(parent,posX,posY );
    }


    public void resetAnimalsDay() {
        animals.forEach(a -> a.resetDayDistance());
        animals.forEach(a -> a.nightHunger());

    }




}
