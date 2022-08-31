package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.island.Cell;
import ru.javarush.ivlev.module2.island.Island;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class IslandStat {
    static NumberFormat formatter = new DecimalFormat("#0.00");
    private final Island island;
    private final PrintStream out;

    public IslandStat(Island island, PrintStream out) {
        this.island = island;
        this.out = out;
    }

    public void printStartDayStatistic(int dayNumber) {
        out.println("Good Morning! Today " + dayNumber + " DAY ");
        printStartDayStatistic();
    }

    public void printStartDayStatistic() {
        out.println("Animal count in island " + island.getAllAnimals().size());
        int cellAnimals = 0;
        for (Cell[] cells : island.getCells()) {
            for (Cell cell : cells) {
                cellAnimals += cell.getAnimals().size();
            }
        }
        out.println("Animal count in cells " + cellAnimals);
        out.println("Plant weight count in island " + formatter.format(island.getAllPlantWeight()));


    }

    public void printEndDayStatistic() {
        int liveAnimalCount = 0;
        int dieAnimalCount = 0;
        for (Animal animal : island.getAllAnimals()) {
            if (animal.isLive()) {
                liveAnimalCount++;
            } else {
                dieAnimalCount++;
            }
        }


        int liveCellAnimals = 0;
        int dieCellAnimals = 0;
        for (Cell[] cells : island.getCells()) {
            for (Cell cell : cells) {
                for (Animal animal : cell.getAnimals()) {
                    if (animal.isLive()) {
                        liveCellAnimals++;
                    } else {
                        dieCellAnimals++;
                    }
                }
            }
        }
        out.println("Live animal count in island " + liveAnimalCount);
        out.println("Die animal in island " + dieAnimalCount);
        out.println("New animal in island " + island.getNewAnimalsToday());

        out.println("Live animal count in cell " + liveCellAnimals);
        out.println("Die animal in cell " + dieCellAnimals);


        out.println("Plant count in island " + formatter.format(island.getAllPlantWeight()));

    }

}
