package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Island;

import java.io.PrintStream;

public class IslandStat {
    private Island island;
    private PrintStream out;

    public IslandStat(Island island, PrintStream out) {

        this.island = island;
        this.out = out;
    }


    public void printStartStatistic() {
        out.println("Today " + island.getDay());
        out.println("Animal count in island " + island.getAllAnimals().size());
        out.println("Plant count in island " + island.getAllAnimals().size());

    }
    public void printEndDayStatistic() {
        out.println("Today " + island.getDay());
        out.println("Animal count in island " + island.getAllAnimals().size());
        out.println("Plant count in island " + island.getAllAnimals().size());

    }
}
