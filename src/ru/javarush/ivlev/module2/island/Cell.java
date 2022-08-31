package ru.javarush.ivlev.module2.island;


import lombok.Getter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.plant.Plant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Cell {
    private final int posX;
    private final int posY;
    Island island;
    @Getter
    List<Animal> animals;
    Plant plant;

    public Cell(Island parent, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.island = parent;
        animals = new ArrayList<>();
        plant = new Plant();
        plant.setCell(this);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public List<Cell> getPossibleDirectionCell(Animal animal) {
        return island.getPossibleDirectionCell(this, animal);
    }


    public boolean canComeIn(Animal animal) {
        int countAnimalsTypeInCell = 0;
        for (Animal animalF : animals) {
            if (animalF.getClass() == animal.getClass()) countAnimalsTypeInCell++;
        }
        return countAnimalsTypeInCell < animal.getMaxCountOnCell();
    }

    public void createAnimals(List<AnimalType> animalTypes) {
        for (AnimalType animaltype : animalTypes) {
            int count = ThreadLocalRandom.current().nextInt(animaltype.getMaxCountOnCell());
            for (int i = 0; i < count; i++) {
                Animal animal = animaltype.getNewAnimal();
                animal.setMaxCountOnCell(animaltype.getMaxCountOnCell());
                animal.setReplete(animal.getSatisfiedWeight()); //  новое сразу сытое
                addAnimalToCell(animal);
                island.addAnimal(animal);
            }
        }
    }

    public void addAnimalToCell(Animal animal) {
        animals.add(animal);
        animal.setCell(this);

    }

    public void growPlant() {
        plant.grow();
    }

    public List<IslandItem> getAptFood(Map<String, Double> canEat) {
        List<IslandItem> res = new ArrayList<>();
        for (String className : canEat.keySet()) {
            for (Animal animal : animals) {
                if (animal.getClass().getName().endsWith(className)) {
                    res.add(animal);
                }
            }
            if (plant.getClass().getName().endsWith(className)) {
                res.add(plant);
            }
        }
        res = res.stream().sorted(Comparator.comparingDouble(IslandItem::getWeight)).collect(Collectors.toList());
        return res;
    }

    public IslandItem getFood(Map<String, Double> canEat) {
        IslandItem result = null;
        for (String className : canEat.keySet()) {
            if (plant.getClass().getName().endsWith(className)) {
                if (plant.getWeight() > 0) result = plant;
            } else {
                for (Animal animal : animals) {
                    if (animal.getClass().getName().endsWith(className)) {
                        if (result == null || result.getWeight() < animal.getWeight()) result = animal;
                    }
                }
            }

        }
        return result;
    }

    public Double getPlantWeiht() {
        return plant.getWeight();
    }

    public Plant getPlant() {
        return plant;
    }


    public Animal getAnimalForMultiplication(Animal animal) {
        for (Animal candidate : animals) {
            if (candidate.isLive()) {
                if (candidate.getClass().getName().equals(animal.getClass().getName())) {
                    if (animal != candidate) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }
}
