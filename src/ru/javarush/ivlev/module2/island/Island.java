package ru.javarush.ivlev.module2.island;

import lombok.Getter;
import ru.javarush.ivlev.module2.animal.Animal;

import java.util.*;

public class Island {

    private final int width;
    private final int height;

    @Getter
    private final Cell[][] cells;
    @Getter
    private final List<Animal> allAnimals;
    private final List<AnimalType> animalTypes;

    public Island(int width, int height, List<AnimalType> animalTypes) {
        this.animalTypes = animalTypes;
        this.width = width;
        this.height = height;
        allAnimals = new LinkedList<>();
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = new IslandCell(this, x, y);
                cell.createAnimals(animalTypes);
                cells[x][y] = cell;
            }
        }
    }


    // думаю что в живой природе животным главное покушать,
// потом размножится и лишь потом, кудато идти
    public void islandDay() {
        long millis = System.currentTimeMillis();

        allAnimals.forEach(Animal::eat);
        System.out.println("eat   "+ (System.currentTimeMillis()-millis));
        millis = System.currentTimeMillis();


        List<Animal> newAnimals = new ArrayList<>();// тут новорожденные
        allAnimals.forEach(animal -> {
            var newAnimal = animal.multiplication();
            if (newAnimal!=null) newAnimals.add(newAnimal);
        });
        allAnimals.addAll(newAnimals);

        System.out.println("multi "+ (System.currentTimeMillis()-millis));
        millis = System.currentTimeMillis();

        allAnimals.forEach(Animal::move);
        System.out.println("move  "+ (System.currentTimeMillis()-millis));
        millis = System.currentTimeMillis();

        dyingOfHunger();
        System.out.println("dyingOfHunger  "+ (System.currentTimeMillis()-millis));
    }

    private void dyingOfHunger() {
        allAnimals.forEach(animal -> {
            if (animal.getReplete() < (animal.getSatisfiedWeight() * 0.001)) animal.die();
        });
    }

    public void islandMorning() {
        long millis = System.currentTimeMillis();
        clearDeadAnimals();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] instanceof IslandCell) {
                    (cells[i][j]).growPlant();
                }
            }
        }
        allAnimals.forEach(Animal::resetDayDistance);
        allAnimals.forEach(Animal::nightHunger);
        System.out.println("morning "+ (System.currentTimeMillis()-millis));
    }

    private void clearDeadAnimals() {


        Iterator<Animal> animalIterator = allAnimals.iterator();
        while (animalIterator.hasNext()){
           // var animal =animalIterator.next() ;
            if(!animalIterator.next().isLive()) {
                animalIterator.remove();
            }
        }

        for (Cell[] cellArray : cells) {
            for (Cell cell : cellArray) {
                animalIterator = cell.animals.iterator();
                while (animalIterator.hasNext()){
                    //var animal = animalIterator.next();
                    if(!animalIterator.next().isLive()) {
                         animalIterator.remove();
                    }
                }
            }

        }
    }



    public List<Direction> getPossibleDirection(Cell cell, Animal animal) {
        List<Direction> res = new ArrayList<>();
        int x = cell.getPosX();
        int y = cell.getPosY();
        if (y > 0) {
            if (cells[x][y - 1].canСomeIn(animal)) {
                res.add(Direction.UP);
            }
        }
        if (x > 0) {
            if (cells[x - 1][y].canСomeIn(animal)) {
                res.add(Direction.LEFT);
            }
        }
        if (y < height - 1) {
            if (cells[x][y + 1].canСomeIn(animal)) {
                res.add(Direction.DOWN);
            }
        }
        if (x < width - 1) {
            if (cells[x + 1][y].canСomeIn(animal)) {
                res.add(Direction.RIGHT);
            }
        }
        return res;
    }

    public boolean animalMove(Animal animal, Direction direction, Cell cell) {
        if (animal.isLive()) {
            return false;
        }
        Cell toCell = switch (direction) {
            case UP -> cells[cell.getPosX()][cell.getPosY() - 1];
            case DOWN -> cells[cell.getPosX()][cell.getPosY() + 1];
            case LEFT -> cells[cell.getPosX() - 1][cell.getPosY()];
            case RIGHT -> cells[cell.getPosX() + 1][cell.getPosY()];
        };
        if (toCell.canСomeIn(animal)) {
            animal.setCell(toCell);
            cell.animals.remove(animal);
            toCell.animals.add(animal);
            return true;
        } else {
            return false;
        }
    }


    public double getAllPlantWeight() {
        double res = 0;
        for (Cell[] cellArray : getCells()) {
            for (Cell cell : cellArray) {
                res += cell.getPlant().getWeight();
            }
        }
        return res;
    }

    public void addAnimal(Animal animal) {
        allAnimals.add(animal);
    }
}
