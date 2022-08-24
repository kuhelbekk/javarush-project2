package ru.javarush.ivlev.module2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ivlev.module2.island.AnimalType;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ParamIslandLoader {
    private String jsonName;
    int heightIsland;
    int widthIsland;
    List<AnimalType> animalTypes;

    public ParamIslandLoader(String jsonName) {

        this.jsonName = jsonName;

        try {
            JsonNode mainNode = new ObjectMapper().readTree(new File(jsonName ));
            widthIsland =  mainNode.get("width").asInt();
            heightIsland = mainNode.get("height").asInt();
            animalTypes = loadAnimals(mainNode.get("animals"));
        } catch (
                JsonProcessingException e) {
            throw new RuntimeException("error Json format");
        } catch (
                IOException e) {
            throw new RuntimeException("error Json file");
        }
    }

    private List<AnimalType> loadAnimals(JsonNode animalsNode) {
        List<AnimalType> animalTypes = new LinkedList<AnimalType>();
        for (JsonNode animalNode : animalsNode) {
            Class clazz = null;
            String className = IslandItem.class.getPackageName() + "." + animalNode.get("className").asText();
            try {
                clazz = Class.forName(className);
                AnimalType animalType = new AnimalType(clazz,animalNode.get("maxCountOnCell").asInt());
                animalType.setClassParams(animalNode.get("classParams"));
                animalTypes.add(animalType);
            } catch (ClassNotFoundException e) {
                //Log("Not found class" + className);
            }
        }
        return animalTypes;
    }

    public List<AnimalType> getAnimalTypes() {
        return animalTypes;
    }

    public int getWidth() {
        return widthIsland;
    }

    public int getHeight() {
        return heightIsland;
    }
}
