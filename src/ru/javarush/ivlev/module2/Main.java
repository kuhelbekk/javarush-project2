package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Island;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Main {
    static Island island;
    public static void main(String[] args)  {
        Properties properties = new Properties();
        Island island = new Island("islandOne.json");
        int steps = 300;
        while(steps>0){
            island.islandDay();
            steps--;
        }

    }




}
