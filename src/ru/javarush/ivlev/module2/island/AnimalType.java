package ru.javarush.ivlev.module2.island;

import com.fasterxml.jackson.databind.JsonNode;
import ru.javarush.ivlev.module2.animal.Animal;
public class AnimalType {
    JsonNode classParams ;
    private final Class<Animal> classAnimal;
    private int мaxCountOnCell ;
    public AnimalType(Class<Animal> classAnimal, int мaxCountOnCell) {
        this.classAnimal = classAnimal;
        this.мaxCountOnCell = мaxCountOnCell;
    }
    public Class<Animal> getClassAnimal() {
        return classAnimal;
    }
    public int getМaxCountOnCell() {
        return мaxCountOnCell;
    }
    public JsonNode getClassParams() {
        return classParams;
    }
    public void setClassParams(JsonNode classParams) {
        this.classParams = classParams;
    }

}
