package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Island;

import java.io.File;

public class Main {
    static Island island;

    public static void main(String[] args) {
        try {
            ParamIslandLoader paramsLoader = new ParamIslandLoader("resources" + File.separator + "islandOne.json");
            new UserDialog(paramsLoader, System.out, System.in, paramsLoader);
            Island island = new Island(paramsLoader.getWidth(), paramsLoader.getHeight(), paramsLoader.getAnimalTypes());
            IslandStat islandStat = new IslandStat(island, System.out);
            island.setIslandStat(islandStat);
            int day=0;
            int steps = 300;
            while (steps > 0 && island.getAllAnimals().size()>0) {
                day++;
                island.islandMorning(); // растет трава, мертвые животные пропадают, а живые набираются сил для перемещений и просыпаются немного голодынми
                islandStat.printStartDayStatistic(day);
                island.islandDay(); // животные едят, размножаются и бегут
                steps--;
                islandStat.printEndDayStatistic();
            }
        } catch (RuntimeException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }


    }


}
