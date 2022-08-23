package ru.javarush.ivlev.module2;

import ru.javarush.ivlev.module2.island.Cell;

public abstract class IslandItem {
    private Cell cell;
    private double weight;

    public IslandItem() {

    }

    public IslandItem(Cell cell) {
        this.cell = cell;
    }


    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public abstract double smallerWeight(double weight);
}
