package ru.javarush.ivlev.module2.island;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ivlev.module2.animal.Animal;

public class AnimalType {
    private final Class<Animal> classAnimal;
    private final int maxCountOnCell;
    Animal clonableAnimalType;
    JsonNode classParams;

    public AnimalType(Class<Animal> classAnimal, int maxCountOnCell) {
        this.classAnimal = classAnimal;
        this.maxCountOnCell = maxCountOnCell;
    }

    public Class<Animal> getClassAnimal() {
        return classAnimal;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public JsonNode getClassParams() {
        return classParams;
    }

    public void setClassParams(JsonNode classParams) {
        try {
            clonableAnimalType = new ObjectMapper().treeToValue(classParams, getClassAnimal());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parse Json Animal params", e);
        }
        this.classParams = classParams;

    }

    public Animal getNewAnimal() {
        try {
            return (Animal) clonableAnimalType.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error clone Animal ", e);
        }
    }
}
