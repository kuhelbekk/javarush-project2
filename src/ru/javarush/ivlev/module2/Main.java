package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Island;

import java.io.File;

public class Main {
    private static final int  YEAR = 365;
    static String defaultSettingsFileName = "resources" + File.separator + "islandOne.json";
    public static void main(String[] args) {
        UserDialog userDialog = new UserDialog( System.out, System.in);
        ParamIslandLoader paramsLoader;
        try {
            paramsLoader = new ParamIslandLoader(userDialog.getFileName(defaultSettingsFileName));
        }catch (RuntimeException e ){
            userDialog.exitWithError("Load options error! ");
            return;
        }
        userDialog.createIsland(paramsLoader.getWidth(), paramsLoader.getHeight()) ;
        Island island = new Island(paramsLoader.getWidth(), paramsLoader.getHeight(), paramsLoader.getAnimalTypes());
        IslandStat islandStat = new IslandStat(island, System.out,userDialog.detailedStatistics());
        int day = 0;
        int maxDay = YEAR;
        while (day<maxDay && island.getAllAnimals().size() > 0) {
            day++;
            island.islandMorning(); // растет трава, мертвые животные пропадают, а живые набираются сил для перемещений и просыпаются немного голодынми
            islandStat.printStartDayStatistic(day);
            long millis = System.currentTimeMillis();
            island.islandDay(); // животные едят, размножаются и бегут
            System.out.println("time day " + (System.currentTimeMillis() - millis));
            islandStat.printStatistic();
        }
    }
}
