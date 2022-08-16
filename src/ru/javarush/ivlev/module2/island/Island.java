package ru.javarush.ivlev.module2.island;

import ru.javarush.ivlev.module2.animal.Animal;

import java.util.ArrayList;

public class Island {
    final int  width;
    final int  height;
    Cell[][] cells;
    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell();

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
                cells[i][j].growPlant();
            }
        }
    }
}
