package ru.javarush.ivlev.module2.animal;

public interface Reproduction {
    <T extends Animal> T reproduction(T animalMale, T animalFemale);
}
