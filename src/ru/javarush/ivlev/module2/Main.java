package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Island;

import java.io.File;
import java.util.Scanner;

public class Main {
    static Island island;
    public static void main(String[] args)  {
        try {
            ParamIslandLoader paramsLoader = new ParamIslandLoader("resources" + File.separator +"islandOne.json");
            new UserDialog(paramsLoader,System.out,System.in, paramsLoader);
            Island island = new Island(paramsLoader.getWidth(),paramsLoader.getHeight(),paramsLoader.getAnimalTypes());
            IslandStat islandStat = new IslandStat(island, System.out);
            islandStat.printStartStatistic();
            int steps = 300;
            while(steps>0){
                island.islandDay();
                steps--;
                islandStat.printEndDayStatistic();
            }
        }catch (RuntimeException e){
            System.out.println("что то пошло не так");
            e.printStackTrace();
        }


    }




}
