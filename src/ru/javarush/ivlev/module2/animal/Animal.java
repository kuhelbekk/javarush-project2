package ru.javarush.ivlev.module2.animal;

import ru.javarush.ivlev.module2.island.Cell;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.island.Direction;

import java.util.List;
import java.util.Map;

public abstract class Animal extends IslandItem implements Move,Eat {
    private double мaxCountOnCell;
    private int maxSpeed;
    private double satisfiedWeight;
    private Map<String,Double> canEat;
    public Map<String, Double> getCanEat() {
        return canEat;
    }

    public void setCanEat(Map<String, Double> canEat) {
        this.canEat = canEat;
    }



    public boolean isLive() {
        return isLive;
    }
    private boolean isLive;
    public Animal() {
        super(null);
        isLive = true;
    }
    public double getМaxCountOnCell() {
        return мaxCountOnCell;
    }

    public void setМaxCountOnCell(double мaxCountOnCell) {
        this.мaxCountOnCell = мaxCountOnCell;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getSatisfiedWeight() {
        return satisfiedWeight;
    }

    public void setSatisfiedWeight(double satisfiedWeight) {
        this.satisfiedWeight = satisfiedWeight;
    }


    @Override
    public boolean eat(IslandItem item) {

        return false;
    }

    @Override
    public void move(Direction direction, int distance) {

    }
}
