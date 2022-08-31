package ru.javarush.ivlev.module2;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class UserDialog {
    private PrintStream out;
    private  InputStream in;
    Scanner scanner;

    public UserDialog(PrintStream out, InputStream in) {
        this.out = out;
        this.in = in;
        scanner = new Scanner(System.in);
    }

    public String getFileName(String defaultSettingsFileName) {
        out.println("Use default island settings from file? "+ defaultSettingsFileName);
        if (!yesNoQuestion()) {
            out.println("enter file name");
            return insertString();
        }
        return defaultSettingsFileName;
    }

    private String insertString() {
        return scanner.next();
    }

    private boolean yesNoQuestion() {
        return  yesNoQuestion(0);
    }
    private boolean yesNoQuestion(int attempt){
        if(attempt>5)throw  new IllegalArgumentException("wrong input");
        out.println("Insert 1 - Yes or 2-No");
        int answer = scanner.nextInt();
        if (answer==1) return true;
        if (answer ==2 ) return false;
        return yesNoQuestion(++attempt);
    }

    public void createIsland(int width, int height) {
        out.println("Create island width "+width+" and height "+height);
    }

    public boolean detailedStatistics() {
        out.println("Use detailed statistics ?");
        return yesNoQuestion();
    }
}
