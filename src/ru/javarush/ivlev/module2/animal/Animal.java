package ru.javarush.ivlev.module2.animal;

import lombok.Getter;
import lombok.Setter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.island.Direction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends IslandItem implements Move,Eat, Cloneable {
    private double мaxCountOnCell; // удалить, так как это не свойство животного, а свойство клетки
    private int maxSpeed;
    private double replete; //сытость
    private double satisfiedWeight;
    @Getter
    private boolean isLive;

    @Getter
    @Setter
    private boolean isMultiplicationInToday;
    boolean canEatPlant;
    int remainingDayDistance;
    /**
     * In cloned animals, the diet changes for everyone at once
     */
    private Map<String,Double> canEat;
    public Map<String, Double> getCanEat() {
        return canEat;
    }

    public void setCanEat(Map<String, Double> canEat) {
        canEatPlant = true;
        canEat.keySet().forEach(nameClass -> { if (nameClass.endsWith("plant.Plant")){ canEatPlant=true;}});
        this.canEat = canEat;
    }


    public Animal() {
        super(null);
        isLive = true;
    }

    @Override
    public double smallerWeight(double weight) {
        isLive = false;
        if (getWeight()<weight){
            Double res  = getWeight();
            setWeight( 0 );
            return res;
        }
        setWeight(getWeight()-weight);
        return weight;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
    public boolean eat() { // остстров спрашивает животное, хочет ли оно кушать..
        if(!isLive) return false;
        if (replete*0.9 >satisfiedWeight) return false;
        List<IslandItem> animalsForFood =  getCell().getAptFood(canEat);// животное хочет есть и спрашивает что в меню
        if (animalsForFood.size()>0) {
            return getCell().eat(this, animalsForFood.get(0));
        }
        return false;
    }

    @Override
    public void move() {
        if (!isLive) return;
        if (remainingDayDistance == 0) return;
        remainingDayDistance--;
        List<Direction> directions = getCell().getPossibleDirection(this);
        if (directions.size()>0){
            Direction dir = directions.get(ThreadLocalRandom.current().nextInt(0,directions.size()));
            getCell().animalMove(this,dir);
        }
    }

    public int getRemainingDayDistance() { // остаток сил на бег
        return remainingDayDistance;
    }

    public boolean isCanEatPlant() {
        return canEatPlant;
    }


    public void resetDayDistance(){
        remainingDayDistance = ThreadLocalRandom.current().nextInt(0, getMaxSpeed() + 1);
    }

    public void nightHunger() {
        replete -= getSatisfiedWeight()/10;
    }
    public void runHunger() {
        replete -= getWeight()/30;
    }

    public double getReplete() {
        return replete;
    }

    public void setReplete(double replete) {
        this.replete = replete;
    }

    public void addReplete(double weight){
        this.replete += weight;
        if ((replete-0.001)>satisfiedWeight){
            replete=satisfiedWeight;
            System.out.println("Животное съело больше чем смогло!");
        }
    }

    public boolean multiplication() {
        if (!isLive) return false;
        /* List<Animal> animalsForMultiplication =  getCell().animalsForMultiplication(this);
        if (animalsForMultiplication.size()>0) {
            return getCell().multiplication(this, animalsForMultiplication.get(0));
        }*/
        return false;
    }

}
