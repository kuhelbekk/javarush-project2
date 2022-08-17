package ru.javarush.ivlev.module2.island;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ivlev.module2.animal.Animal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Island {
    final int  width;
    final int  height;
    Cell[][] cells;

    List<AnimalType> animalTypes ;

    public Island(String fileParamName) {



        try {
            JsonNode mainNode = new ObjectMapper().readTree(new File("properties" + File.separator +fileParamName ));
            this.width =  mainNode.get("width").asInt();
            this.height = mainNode.get("height").asInt();
            animalTypes = loadAnimals(mainNode.get("animals"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error Json format");
        } catch (IOException e) {
            throw new RuntimeException("error Json file");
        }



        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new IslandCell(animalTypes);
            }
        }

    }

    public  boolean move(Animal animal, Direction direction){
        if (animal.isLive()){

        }
        return true;
    }


    public void startDay() {
        startedGrowPlant();
    }

    private void startedGrowPlant() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] instanceof IslandCell)
                    ((IslandCell) cells[i][j]).growPlant();
            }
        }
    }

    private List<AnimalType> loadAnimals(JsonNode animalsNode) {
        List<AnimalType> animalTypes = new LinkedList<AnimalType>();
        for (JsonNode animalNode : animalsNode) {
            Class clazz = null;
            String className = Animal.class.getPackageName() + "." + animalNode.get("className").asText();
            try {
                clazz = Class.forName(className);
                AnimalType animalType = new AnimalType(clazz,animalNode.get("мaxCountOnCell").asInt());
                animalType.setClassParams(animalNode.get("classParams"));
                animalTypes.add(animalType);
            } catch (ClassNotFoundException e) {
                //Log("Not found class" + className);
            }
        }
        return animalTypes;
    }
}
