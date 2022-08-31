package ru.javarush.ivlev.module2.island;

import lombok.Getter;
import lombok.Setter;
import ru.javarush.ivlev.module2.IslandStat;
import ru.javarush.ivlev.module2.animal.Animal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Island {

    private final int width;
    private final int height;
    @Getter
    private final Cell[][] cells;
    @Getter
    private final List<Animal> allAnimals;
    private final List<AnimalType> animalTypes;
    @Setter
    IslandStat islandStat;
    @Getter
    private int newAnimalsToday;

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


    public void islandDay() {
        long millis = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (Animal animal : allAnimals) {
            executor.submit(animal::eat);
        }
        executor.shutdown();
        for (Animal animal : allAnimals) {
            animal.move();
        }
        System.out.println("move done" + (System.currentTimeMillis() - millis));
        try {
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.out.println("eat done" + (System.currentTimeMillis() - millis));
        var newAnimals = multiplication();
        dyingOfHunger();
        newAnimalsToday = newAnimals.size();
        allAnimals.addAll(newAnimals);
        System.out.println("time day " + (System.currentTimeMillis() - millis));

        //millis = System.currentTimeMillis();

    }

    private int dyingOfHunger() {
        AtomicInteger count = new AtomicInteger();
        allAnimals.forEach(animal -> {
            if (animal.isLive() && animal.getReplete() < (animal.getSatisfiedWeight() * 0.001)) {
                animal.die();
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    List<Animal> multiplication() {
        List<Animal> newAnimals = new ArrayList<>();// тут новорожденные
        allAnimals.forEach(animal -> {
            var newAnimal = animal.multiplication();
            if (newAnimal != null) newAnimals.add(newAnimal);
        });
        return newAnimals;

    }

    public void islandMorning() {
        clearDeadAnimals();
        newAnimalsToday = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] instanceof IslandCell) {
                    (cells[i][j]).growPlant();
                }
            }
        }
        allAnimals.forEach(Animal::resetDayDistance);
        allAnimals.forEach(Animal::nightHunger);
    }

    private void clearDeadAnimals() {


        Iterator<Animal> animalIterator = allAnimals.iterator();
        while (animalIterator.hasNext()) {
            // var animal =animalIterator.next() ;
            if (!animalIterator.next().isLive()) {
                animalIterator.remove();
            }
        }

        for (Cell[] cellArray : cells) {
            for (Cell cell : cellArray) {
                animalIterator = cell.animals.iterator();
                while (animalIterator.hasNext()) {
                    //var animal = animalIterator.next();
                    if (!animalIterator.next().isLive()) {
                        animalIterator.remove();
                    }
                }
            }

        }
    }


    public List<Cell> getPossibleDirectionCell(Cell cell, Animal animal) {
        List<Cell> res = new ArrayList<>();
        int x = cell.getPosX();
        int y = cell.getPosY();
        if (y > 0) {
            if (cells[x][y - 1].canComeIn(animal)) {
                res.add(cells[x][y - 1]);
            }
        }
        if (x > 0) {
            if (cells[x - 1][y].canComeIn(animal)) {
                res.add(cells[x - 1][y]);
            }
        }
        if (y < height - 1) {
            if (cells[x][y + 1].canComeIn(animal)) {
                res.add(cells[x][y + 1]);
            }
        }
        if (x < width - 1) {
            if (cells[x + 1][y].canComeIn(animal)) {
                res.add(cells[x + 1][y]);
            }
        }
        return res;
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
