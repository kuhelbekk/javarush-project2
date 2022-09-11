package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.animal.Animal;
import ru.javarush.ivlev.module2.island.AnimalType;
import ru.javarush.ivlev.module2.island.Cell;
import ru.javarush.ivlev.module2.island.Island;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class IslandStat {
    private static NumberFormat formatter = new DecimalFormat("#0.00");
    private final Island island;
    private final PrintStream out;
    private boolean detailedStatistics;

    public IslandStat(Island island, PrintStream out, boolean detailedStatistics) {
        this.island = island;
        this.out = out;
        this.detailedStatistics = detailedStatistics;
    }

    public void printStartDayStatistic(int dayNumber) {
        out.println("Good Morning! Today " + dayNumber + " DAY ");
        printStatistic();
    }



    public void printStatistic() {
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
        if (dieAnimalCount>0) out.println("Die animal in island " + dieAnimalCount);
        if (island.getNewAnimalsToday()>0) out.println("New animal in island " + island.getNewAnimalsToday());
        out.println("Plant count in island " + formatter.format(island.getPlantWeight()));
        if(detailedStatistics) detailStat();

    }

    private void detailStat() {
        Map<AnimalType, Integer> liveAnamals = new HashMap<>();
        Map<AnimalType, Integer> dieAnamals = new HashMap<>();
        for (Cell[] cells : island.getCells()) {
            for (Cell cell : cells) {
                for (Animal animal : cell.getAnimals()) {
                    if (animal.isLive()){
                        Integer count = liveAnamals.get(animal.getType());
                        if(count==null) {
                            liveAnamals.put(animal.getType(), 1);
                        }else{
                            liveAnamals.put(animal.getType(),++count);
                        }
                    }else{
                        Integer count = dieAnamals.get(animal.getType());
                        if(count==null) {
                            dieAnamals.put(animal.getType(), 1);
                        }else{
                            dieAnamals.put(animal.getType(),++count);
                        }
                    }
                }
            }
        }
        for (AnimalType animalType : island.getAnimalTypes()) {
            Integer live = liveAnamals.get(animalType);
            Integer die = dieAnamals.get(animalType);
            if(die!=null || live!=null){
                out.print(animalType.getShortName());
                if(live!=null) out.print(":"+live);
                if(die!=null) out.print(" die- "+die);
                out.print("  ");
            }
        }
        out.println(" ");
    }
}
