package ru.javarush.ivlev.module2.island;


import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.plant.Plant;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Cell {

    Island parent;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    private int posX;
    private int posY;
    List<Animal> animals;
    Plant plant;

    List<AnimalType> animaltypes;
    public Cell(Island parent, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.parent = parent;
        animals = new LinkedList<>();
        plant = new Plant();
        plant.setCell(this);
    }

    public List<Direction> getPossibleDirection(Animal animal) {
        return  parent.getPossibleDirection(this,animal);
    }

    public boolean animalMove(Animal animal, Direction direction) {
       return parent.animalMove(animal,direction,this);
    }

    public boolean canСomeIn(Animal animal) {
        int countAnimalsTypeInCell=0;

        for (Animal animalF : animals) {
            if (animalF.getClass()==animal.getClass()) countAnimalsTypeInCell++;
        }
        if(countAnimalsTypeInCell<animal.getМaxCountOnCell()) return true;


        return false;
    }


    public void createAnimals(List<AnimalType> animaltypes) {
        createAnimals(animaltypes,null);
    }


    public void createAnimals(List<AnimalType> animaltypes, Set<Animal> setFolAnimals) {
        for (AnimalType animaltype : animaltypes) {
            int count = new Random().nextInt(animaltype.getМaxCountOnCell());
            for (int i = 0; i < count; i++) {
                Animal animal = animaltype.getNewAnimal();
                animal.setМaxCountOnCell(animaltype.getМaxCountOnCell());
                animal.setReplete(animal.getSatisfiedWeight()); //  новое сразу сытое
                animal.setCell(this);
                animals.add(animal);
                if (setFolAnimals != null) setFolAnimals.add(animal);
            }
        }
    }
    public void growPlant() {
        plant.grow();
    }

    public List<IslandItem> getAptFood(Map<String, Double> canEat) {
        List<IslandItem> res = new ArrayList<>();
        canEat.keySet().forEach(className ->{
            for (Animal animal : animals) {
                if( animal.getClass().getName().endsWith(className)) res.add(animal);
            }
            if (plant.getClass().getName().endsWith(className)) res.add(plant);
        });
        res.stream().sorted(Comparator.comparingDouble(IslandItem::getWeight)).collect(Collectors.toList());
        return res;
    }

    public Double getPlantWeiht() {
        return plant.getWeight();
    }

    public Plant getPlant() {
        return plant;
    }

    public boolean eat(Animal animal, IslandItem foodItem) {
        if (animal.getCell() == this && foodItem.getCell() == this){
            for (String className : animal.getCanEat().keySet()) {
                if (foodItem.getClass().getName().endsWith(className)){
                    if(ThreadLocalRandom.current().nextDouble()<animal.getCanEat().get(className)){ //dice
                        //foodItem.die(); //  с травой костыль вышел, но я ченть придумаю
                        animal.addReplete( foodItem.smallerWeight(animal.getSatisfiedWeight() - animal.getReplete()) );
                        return true;
                    }
                }
            }
        }
        return false;


    }
}
