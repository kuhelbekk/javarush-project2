package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.island.Cell;
import ru.javarush.ivlev.module2.island.Island;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class IslandStat {
    private final Island island;
    private final PrintStream out;

    static NumberFormat formatter = new DecimalFormat("#0.00");

    public IslandStat(Island island, PrintStream out) {
        this.island = island;
        this.out = out;
    }

    public void printStartDayStatistic(int dayNumber) {
        out.println("Good Moning! Today " + dayNumber + " DAY ");
        printStartDayStatistic();
    }
    public void printStartDayStatistic() {
        out.println("Animal count in island " + island.getAllAnimals().size());
        for (Cell[] cells : island.getCells()) {
            for (Cell cell : cells) {
                cell.ani
            }
        }
        out.println("Plant weight count in island " + formatter.format(island.getAllPlantWeight()));


    }

    public void printEndDayStatistic() {
        int liveAnimalCount = 0;
        int dieAnimalCount = 0;
        for (Animal animal : island.getAllAnimals()) {
            if (animal.isLive()){
                liveAnimalCount++;
            }else {
                dieAnimalCount++;
            }
        }
        out.println("Live animal count in island " + liveAnimalCount);
        out.println("Die animal in islend " + dieAnimalCount);
        out.println("Plant count in island " + formatter.format(island.getAllPlantWeight()));

    }

}
