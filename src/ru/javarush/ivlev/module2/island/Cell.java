package ru.javarush.ivlev.module2.island;


import lombok.Getter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.plant.Plant;

import java.util.*;
import java.util.stream.Collectors;

public class Cell {
    Island island;
    @Getter
    List<Animal> animals;
    Plant plant;
 //   List<AnimalType> animaltypes;
    private final int posX;
    private final int posY;
    public Cell(Island parent, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.island = parent;
        animals = new LinkedList<>();
        plant = new Plant();
        plant.setCell(this);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public List<Direction> getPossibleDirection(Animal animal) {
        return island.getPossibleDirection(this, animal);
    }

    public boolean animalMove(Animal animal, Direction direction) {
        return island.animalMove(animal, direction, this);
    }

    public boolean canСomeIn(Animal animal) {
        int countAnimalsTypeInCell = 0;
        for (Animal animalF : animals) {
            if (animalF.getClass() == animal.getClass()) countAnimalsTypeInCell++;
        }
        return countAnimalsTypeInCell < animal.getMaxCountOnCell();
    }

    public void createAnimals(List<AnimalType> animalTypes) {
        for (AnimalType animaltype : animalTypes) {
            int count = new Random().nextInt(animaltype.getMaxCountOnCell());
            for (int i = 0; i < count; i++) {
                Animal animal = animaltype.getNewAnimal();
                animal.setMaxCountOnCell(animaltype.getMaxCountOnCell());
                animal.setReplete(animal.getSatisfiedWeight()); //  новое сразу сытое
                addAnimalToCell(animal);
                island.addAnimal(animal);
            }
        }
    }

    public void addAnimalToCell(Animal animal){
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
                if (animal.getClass().getName().endsWith(className)){
                    res.add(animal);
                }
            }
            if (plant.getClass().getName().endsWith(className)){
                res.add(plant);
            }
        }
        res = res.stream().sorted(Comparator.comparingDouble(IslandItem::getWeight)).collect(Collectors.toList());
        return res;
    }

    public IslandItem getFood(Map<String, Double> canEat) {
        IslandItem res =null;
        for (String className : canEat.keySet()) {
            if (plant.getClass().getName().endsWith(className)){
                if (plant.getWeight()>0) res = plant;
            }else {
                for (Animal animal : animals) {
                    if (animal.getClass().getName().endsWith(className)) {
                        if (res == null || res.getWeight() < animal.getWeight()) res = animal ;
                    }
                }
            }

        }
        return res;
    }

    public Double getPlantWeiht() {
        return plant.getWeight();
    }

    public Plant getPlant() {
        return plant;
    }


//    public List<Animal> animalsForMultiplication(Animal animal) {
//        List<Animal> candidateList = new ArrayList<>();
//        for (Animal candidate : animals) {
//            if (candidate.isLive()) {
//                if ((candidate.getClass().getName().equals(animal.getClass().getName())) && (animal != candidate)) {
//                    candidateList.add(candidate);
//                }
//            }
//        }
//        //  берем самого сытого
//        candidateList = candidateList.stream().sorted(Comparator.comparingDouble(Animal::getReplete)).collect(Collectors.toList());
//        return candidateList;
//    }

    public Animal getAnimalForMultiplication(Animal animal) {
        for (Animal candidate : animals) {
            if (candidate.isLive()) {
                if ((candidate.getClass().getName().equals(animal.getClass().getName())) && (animal != candidate)) {
                    return  candidate;
                }
            }
        }
        return null;
    }
}
