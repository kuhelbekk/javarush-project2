package ru.javarush.ivlev.module2.animal;

import lombok.Getter;
import lombok.Setter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.island.Direction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends IslandItem implements Move,Eat, Cloneable {
    @Getter
    @Setter
    private double maxCountOnCell; // удалить, так как это не свойство животного, а свойство клетки
    @Getter
    @Setter
    private int maxSpeed;
    @Getter
    @Setter
    private double replete; //сытость
    @Getter
    @Setter
    private double satisfiedWeight;
    @Getter
    private boolean isLive;

    @Getter
    @Setter
    private boolean isMultiplicationInToday;
    @Getter
    boolean canEatPlant;
    @Getter
    int remainingDayDistance;
    /**
     * In cloned animals, the diet changes for everyone at once
     */
    @Getter
    private Map<String,Double> canEat;

    public void setCanEat(Map<String, Double> canEat) {
        canEatPlant = false;
        if(canEat.containsKey( "plant.Plant") ){
            canEatPlant=true;
        }
        this.canEat = canEat;
    }


    public Animal() {
        super(null);
        isLive = true;
    }

    @Override
    public double smallerWeight(double weight) {
        die();
        if (getWeight()<weight){
            Double res  = getWeight();
            setWeight( 0 );
            return res;
        }
        setWeight(getWeight()-weight);
        return weight;
    }

    public void die() {
        isLive = false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }



    @Override
    public boolean eat() { // остстров спрашивает животное, хочет ли оно кушать..
        if(!isLive) return false;
        if (replete*0.9 >satisfiedWeight) return false;
        List<IslandItem> animalsForFood = getCell().getAptFood(canEat);// животное хочет есть и спрашивает что в меню
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


    public void resetDayDistance(){
        remainingDayDistance = ThreadLocalRandom.current().nextInt(0, getMaxSpeed() + 1);
    }

    public void nightHunger() {
        replete -= getSatisfiedWeight()/10;
    }
    public void runHunger() {
        replete -= getWeight()/30;
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
