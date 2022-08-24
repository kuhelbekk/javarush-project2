package ru.javarush.ivlev.module2.island;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import ru.javarush.ivlev.module2.IslandItem;
import ru.javarush.ivlev.module2.animal.Animal;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Island {

    @Getter
    private int day;
    private final int width;
    private final int height;
    @Getter
    private Cell[][] cells;
    @Getter
    private Set<Animal> allAnimals;
    private List<AnimalType> animalTypes ;

    public Island(int width, int height, List<AnimalType> animalTypes) {
        day = 0;
        this.animalTypes=animalTypes;
        this.width = width;
        this.height = height;
        allAnimals = new HashSet<>();
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = new IslandCell(this ,x,y );
                cell.createAnimals( animalTypes,allAnimals );
                cells[x][y] = cell;
            }
        }
    }





// думаю что в живой природе животным главное покушать,
// потом размножится и лишь потом, кудато идти(и то лишь при условии что кушать и размножатся в этой местности стало тяжелее )
    public void islandDay(){
        startDay();
        everydayRoutine();
        // cells
    }
    public void startDay() {
        day++;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] instanceof IslandCell) {
                    ((IslandCell) cells[i][j]).growPlant();
                    ((IslandCell) cells[i][j]).resetAnimalsDay();
                }
            }
        }
    }
    private void everydayRoutine() {
        allAnimals.forEach(animal -> animal.eat());
        allAnimals.forEach(animal -> animal.multiplication());
        allAnimals.forEach(animal -> animal.move());

    }

    public  List<Direction> getPossibleDirection(Cell cell, Animal animal) {
        List<Direction> res = new ArrayList<>();
        int x = cell.getPosX();
        int y = cell.getPosY();
        if (y>0){
            if (cells[x][y-1].canСomeIn(animal)){
                res.add(Direction.UP);
            }
        }
        if (x>0){
            if (cells[x-1][y].canСomeIn(animal)){
                res.add(Direction.LEFT);
            }
        }
        if (y<height-1) {
            if (cells[x][y+1].canСomeIn(animal)){
                res.add(Direction.DOWN);
            }
        }
        if (x<width-1) {
            if (cells[x+1][y].canСomeIn(animal)){
                res.add(Direction.RIGHT);
            }
        }
        return res;
    }

    public boolean animalMove(Animal animal, Direction direction, Cell cell) {
        if (animal.isLive()){
            return false;
        }
        Cell toCell = switch (direction){
            case UP ->  cells[cell.getPosX()][cell.getPosY()-1];
            case DOWN ->  cells[cell.getPosX()][cell.getPosY()+1];
            case LEFT ->  cells[cell.getPosX()-1][cell.getPosY()];
            case RIGHT -> cells[cell.getPosX()+1][cell.getPosY()];
        };
        if (toCell.canСomeIn(animal)){
            animal.setCell(toCell);
            cell.animals.remove(animal);
            toCell.animals.add(animal);
            return true;
        }else{
            return false;
        }
    }


}
