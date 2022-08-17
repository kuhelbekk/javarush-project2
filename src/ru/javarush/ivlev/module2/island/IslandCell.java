package ru.javarush.ivlev.module2.island;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.animal.Animal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IslandCell extends Cell {
    List<Animal> animals;
    Plant plant;
    public IslandCell(List<AnimalType> animaltypes) {
        animals = new ArrayList<>();
        plant = new Plant();
        createAnimals( animaltypes );

    }

    private void createAnimals(List<AnimalType> animaltypes) {
        for (AnimalType animaltype : animaltypes) {
            int count = new Random().nextInt(animaltype.getМaxCountOnCell());
            try {
                for (int i = 0; i < count; i++) {
                    Animal animal = new ObjectMapper().treeToValue(animaltype.getClassParams(), animaltype.getClassAnimal());
                    animal.setCell(this);
                    animals.add(animal);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error json from create Animal",e);
            }
        }
    }
    public void growPlant() {
        plant.grow();
    }
}
