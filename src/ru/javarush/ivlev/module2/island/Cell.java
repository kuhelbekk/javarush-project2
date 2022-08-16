package ru.javarush.ivlev.module2.island;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.animal.herbivores.Sheep;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Cell {
    List<Animal> animals;
    Plant plant;





    public Cell() {
        animals = new    ArrayList<>();
        plant = new Plant();

        ObjectMapper mapper = new ObjectMapper();//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode jsonNode = mapper.readTree(new File("properties"+File.separator+Sheep.class.getName()+".json");
            Animal animal =mapper.readValue(jsonNode.asText("{}"),Sheep.class);
            mapper.readValue(Reader.nullReader())
                   // mapper.readValue(new File("properties"+File.separator+Sheep.class.getName()+".json"), Sheep.class);
            mapper.reader().rea
            animal.setCell(this);
            animals.add( animal);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error file format");
        } catch (IOException e) {
            e.printStackTrace();
           throw new RuntimeException("error, no file");
        }
    }

    public void growPlant() {
        plant.grow();

    }
}
