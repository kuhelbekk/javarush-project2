package ru.javarush.ivlev.module2.animal;

import lombok.Getter;
import lombok.Setter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.island.Cell;
import ru.javarush.ivlev.module2.island.Direction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public abstract class Animal extends IslandItem implements Move, Eat, Cloneable {
    @Getter
    boolean canEatPlant;
    @Getter
    int remainingDayDistance;
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
    private boolean isMultiplied;
    /**
     * In cloned animals, the diet changes for everyone at once
     */
    @Getter
    private Map<String, Double> canEat;

    private static Logger log = Logger.getLogger(Animal.class.getName());

    public Animal() {
        super(null);
        isLive = true;
    }

    public void setCanEat(Map<String, Double> canEat) {
        canEatPlant = false;
        if (canEat.containsKey("plant.Plant")) {
            canEatPlant = true;
        }
        this.canEat = canEat;
    }

    @Override
    public double smallerWeight(double weight) {
        die();
        if (getWeight() < weight) {
            Double res = getWeight();
            setWeight(0);
            return res;
        }
        setWeight(getWeight() - weight);
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
        if (!isLive) return false;
        if (replete * 0.9 > satisfiedWeight) return false;
        // List<IslandItem> animalsForFood = getCell().getAptFood(canEat);// животное хочет есть и спрашивает что в меню
        IslandItem itemForFood = getCell().getFood(canEat);// животное хочет есть и спрашивает что в меню

        if (itemForFood != null) {
            synchronized (itemForFood) {
                if (isCanEat(itemForFood)) {
                    addReplete(itemForFood.smallerWeight(getSatisfiedWeight() - getReplete()));
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    boolean isCanEat(IslandItem itemForFood) {
        if (getCell() == itemForFood.getCell() && isLive) {
            for (String className : getCanEat().keySet()) {
                if (itemForFood.getClass().getName().endsWith(className)) {
                    if (ThreadLocalRandom.current().nextDouble() < getCanEat().get(className)) { //dice
                        return true;
                    }
                }
            }
        }
        return false;
    }




    @Override
    public void move() {
        if (!isLive) return;
        while (remainingDayDistance > 0 && replete > 0) { // если Животное истощено, то не ходит
            List<Cell> possibleCellForMove = getCell().getPossibleDirectionCell(this);
            if (possibleCellForMove.size() > 0) {
                Cell toCell = possibleCellForMove.get(ThreadLocalRandom.current().nextInt(0, possibleCellForMove.size()));
                synchronized (toCell){
                    synchronized (this){
                        remainingDayDistance--;
                        if (!isLive())  return ;
                        if (toCell.canСomeIn(this)) {
                            getCell().getAnimals().remove(this);
                            setCell(toCell);
                            toCell.getAnimals().add(this);
                        }
                        moveHunger();
                    }
                }
            }else{
                remainingDayDistance=0;
            }
        }

    }


    public void resetDayDistance() {
        remainingDayDistance = ThreadLocalRandom.current().nextInt(0, getMaxSpeed() + 1);
    }

    public void nightHunger() {
        replete -= getSatisfiedWeight() / 10;
        if (replete < 0) replete = 0;
    }

    public void moveHunger() {
        replete -= getWeight() / 30;
        if (replete < 0) replete = 0;
    }


    public void addReplete(double weight) {
        this.replete += weight;
        if ((replete - 0.001) > satisfiedWeight) {
            replete = satisfiedWeight;
            System.out.println("Животное съело больше чем смогло!");
        }
    }

    public Animal multiplication() {

        Animal animalForMultiplication = getCell().getAnimalForMultiplication(this);
        if (animalForMultiplication == null) return null;
        Animal a1,a2;
        if (animalForMultiplication.hashCode()>hashCode()){
            a1 = this;
            a2 = animalForMultiplication;
        }else {
            a2 = this;
            a1 = animalForMultiplication;
        }
        synchronized (a1){
            synchronized (a2){
                synchronized (getCell()) {
                    if (!isReadyReproduction(animalForMultiplication)) return null;
                    return getBaby(animalForMultiplication);
                }
            }
        }


    }


    boolean isReadyReproduction(Animal animal) {

            return  animal.isLive() &&
                    isLive() &&
                    !isMultiplied()&&
                    !animal.isMultiplied() &&
                    animal.getCell() == getCell() &&
                    animal.getReplete()>0.01 &&
                    getReplete()>0.01 ;

    }

    public Animal getBaby(Animal animalForMultiplication){
        setMultiplied(true);
        animalForMultiplication.setMultiplied(true);
        if (getCell().canСomeIn(this)) {
            if (ThreadLocalRandom.current().nextDouble() > 0.5) {//dice 50/50
                try {
                    Animal newAnimal = (Animal) this.clone();
                    newAnimal.replete = newAnimal.satisfiedWeight;
                    newAnimal.remainingDayDistance = 0; // родвшееся животное никуда не ходит, а растет всю ночь, и ждет пока окрепнут ноги.
                    animalForMultiplication.moveHunger();// на размножение тоже требуется энергия
                    moveHunger();
                    getCell().addAnimalToCell(newAnimal);
                    return newAnimal;
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException("Error clone animal", e);
                }
            }
        }
        return null;
    }
}
