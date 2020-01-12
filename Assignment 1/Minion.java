/*
* Johnny Doan
* */

import javax.swing.*;

public class Minion {
    private String name;
    private double height;
    private int numberOfEvilDeeds;

    public Minion(String name, double height) {
        this.name = name;
        this.height = height;
        this.numberOfEvilDeeds = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getNumberOfEvilDeeds() {
        return numberOfEvilDeeds;
    }

    public void setNumberOfEvilDeeds(int numberOfEvilDeeds) {
        this.numberOfEvilDeeds = numberOfEvilDeeds;
    }

    @Override
    public String toString() {
        return "MinionInfo{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", numberOfEvilDeeds=" + numberOfEvilDeeds +
                '}';
    }
}
