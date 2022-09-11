package ru.javarush.ivlev.module2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import ru.javarush.ivlev.module2.island.AnimalType;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ParamIslandLoader {
    @Getter
    private int height;
    @Getter
    private int width;
    private List<AnimalType> animalTypes;

    public ParamIslandLoader(String jsonName) {
        try {
            JsonNode mainNode = new ObjectMapper().readTree(new File(jsonName));
            width = mainNode.get("width").asInt();
            height = mainNode.get("height").asInt();
            animalTypes = loadAnimals(mainNode.get("animals"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error Json format", e);
        } catch (IOException e) {
            throw new RuntimeException("error Json file", e);
        }
    }

    private List<AnimalType> loadAnimals(JsonNode animalsNode) {
        List<AnimalType> animalTypes = new LinkedList<AnimalType>();
        for (JsonNode animalNode : animalsNode) {
            Class clazz = null;
            String className = IslandItem.class.getPackageName() + "." + animalNode.get("className").asText();
            try {
                clazz = Class.forName(className);
                AnimalType animalType = new AnimalType(clazz, animalNode.get("maxCountOnCell").asInt());
                animalType.setEmoji(animalNode.get("emoji").asText());
                animalType.setShortName(animalNode.get("shortName").asText());
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


}
