package ru.javarush.ivlev.module2.island;

import ru.javarush.ivlev.module2.IslandItem;

import java.util.Random;

public class Plant extends IslandItem {
   double мaxWeightOnCell;

    public Plant() {
        grow();

    }

    public void grow() {
        setWeight(getWeight()+ мaxWeightOnCell * new Random().nextDouble());
    }
}
