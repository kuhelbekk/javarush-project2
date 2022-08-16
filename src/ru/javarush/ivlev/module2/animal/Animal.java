package ru.javarush.ivlev.module2.animal;

import ru.javarush.ivlev.module2.island.Cell;
import ru.javarush.ivlev.module2.IslandItem;

public abstract class Animal extends IslandItem implements Move,Eat {
    public double мaxCountOnCell;
    public int maxSpeed;
    public double satisfiedWeight;

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


}
