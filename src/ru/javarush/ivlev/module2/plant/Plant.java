package ru.javarush.ivlev.module2.plant;

import ru.javarush.ivlev.module2.IslandItem;

import java.util.Random;

public class Plant extends IslandItem {
    double maxWeightOnCell;

    public Plant() {
        maxWeightOnCell = 200; // todo  грузить из настроек
        grow();
    }

    @Override
    public double smallerWeight(double weight) {
        if (getWeight() < weight) {
            Double res = getWeight();
            setWeight(0);
            return res;
        }
        setWeight(getWeight() - weight);
        return weight;
    }

    public void grow() {
        double newWeight = getWeight() + maxWeightOnCell * new Random().nextDouble();
        if (newWeight> maxWeightOnCell) newWeight = maxWeightOnCell;
        setWeight(newWeight);
    }
}
