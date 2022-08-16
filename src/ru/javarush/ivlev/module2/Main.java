package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Island;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Main {
    static Island island;
    public static void main(String[] args) {
        Properties properties = new Properties();

        try(FileReader fileReader = new FileReader("island.properties")){
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int  width = Integer.parseInt(properties.getProperty("width", "20"));
        int  height = Integer.parseInt(properties.getProperty("height", "100"));
        Island island = new Island(width,height);

        int steps = 300;
        while(steps>0){
            island.startDay();
            steps--;
        }

    }




}
