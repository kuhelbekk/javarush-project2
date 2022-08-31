package ru.javarush.ivlev.module2.animal;

import lombok.Getter;
import lombok.Setter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.island.AnimalType;
import ru.javarush.ivlev.module2.island.Cell;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public abstract class Animal extends IslandItem implements Move, Eat, Cloneable {
    private static final Logger log = Logger.getLogger(Animal.class.getName());
    private static final double NIGHT_HUNGER_MULTIPLIER = 0.1;
    private static final double MOVE_HUNGER_MULTIPLIER = 0.05;
    private static final double CHANCE_OF_REPRODUCTION = 0.9;
    @Getter
    boolean canEatPlant;
    @Getter
    int remainingDayDistance;
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
    @Getter
    @Setter
    AnimalType type;
    /**
     * In cloned animals, the diet changes for everyone at once
     */
    @Getter
    private Map<String, Double> canEat;

    public Animal() {
        super(null);
        isLive = true;
    }

    public void setCanEat(Map<String, Double> canEat) {
        canEatPlant = canEat.containsKey("plant.Plant");
        this.canEat = canEat;
    }

    @Override
    public double smallerWeight(double weight) {
        die();
        if (getWeight() < weight) {
            double res = getWeight();
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
    public boolean eat() {
        if (!isLive) return false;
        if (replete * 0.9 > satisfiedWeight) return false;
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
                Cell c1, c2;
                if (toCell.hashCode() > getCell().hashCode()) {
                    c1 = getCell();
                    c2 = toCell;
                } else {
                    c2 = getCell();
                    c1 = toCell;
                }
                synchronized (c1) {
                    synchronized (c2) {
                        // log.info("thread:" +Thread.currentThread().getName() +c1.hashCode()+" "+c2.hashCode());

                        remainingDayDistance--;
                        if (!isLive()) return;
                        if (toCell.canComeIn(this)) {
                            getCell().getAnimals().remove(this);
                            toCell.addAnimalToCell(this);
                        }
                        moveHunger();
                    }
                }
            } else {
                remainingDayDistance = 0;
            }
        }

    }


    public void resetDayDistance() {
        remainingDayDistance = ThreadLocalRandom.current().nextInt(0, getMaxSpeed() + 1);
    }

    public void nightHunger() {
        replete -= getSatisfiedWeight() * NIGHT_HUNGER_MULTIPLIER;
        if (replete < 0) replete = 0;
    }

    public void moveHunger() {
        replete -= getWeight() * MOVE_HUNGER_MULTIPLIER;
        if (replete < 0) replete = 0;
    }


    public void addReplete(double weight) {
        this.replete += weight;
        if ((replete - MOVE_HUNGER_MULTIPLIER) > satisfiedWeight) {
            replete = satisfiedWeight;
        }
    }

    public Animal multiplication() {

        Animal result = null;
        Animal animalForMultiplication = null;
        synchronized (getCell()) {
            animalForMultiplication = getCell().getAnimalForMultiplication(this);
        }
        if (animalForMultiplication == null) return null;
        Animal a1, a2;
        if (animalForMultiplication.hashCode() > hashCode()) {
            a1 = this;
            a2 = animalForMultiplication;
        } else {
            a2 = this;
            a1 = animalForMultiplication;
        }
        synchronized (a1) {
            synchronized (a2) {
                synchronized (getCell()) {
                    //log.info("thread:" +Thread.currentThread().getName() + getCell().hashCode());
                    if (isReadyReproduction(animalForMultiplication)) result = getBaby(animalForMultiplication);
                }
            }
        }
        return result;
    }


    boolean isReadyReproduction(Animal animal) {
        return animal.isLive() &&
                isLive() &&
                !isMultiplied() &&
                !animal.isMultiplied() &&
                animal.getCell() == getCell() &&
                animal.getReplete() > MOVE_HUNGER_MULTIPLIER &&
                getReplete() > MOVE_HUNGER_MULTIPLIER;

    }

    public Animal getBaby(Animal animalForMultiplication) {
        setMultiplied(true);
        animalForMultiplication.setMultiplied(true);
        if (getCell().canComeIn(this)) {
            if (ThreadLocalRandom.current().nextDouble() > CHANCE_OF_REPRODUCTION) {//dice
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
