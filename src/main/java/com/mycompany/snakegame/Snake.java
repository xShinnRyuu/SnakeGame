package com.mycompany.snakegame;
/**
 * This will be a beginners personal project to the 1997 Nokia Snake game
 */

public class Snake {
    final double SNAKE_SIZE = 20;
    private double currX, currY;
    private int growth;

    public Snake() {
        currX = 0;
        currY = 0;
        growth = 0;
    }

    public Snake(double currX, double currY, boolean center) {
        this.currX = currX/2;
        this.currY = currY/2;
        growth = 0;
    }
    
    public Snake(double currX, double currY) {
        this.currX = currX;
        this.currY = currX;
        growth = 0;
    }
    

    double getX() {
        return currX;
    }

    double getXPlusSize() {
        return currX + SNAKE_SIZE/2;
    }

    double getXMinusSize() {
        return currX - SNAKE_SIZE/2;
    }

    void setX(double x) {
        currX = x;
    }

    double getY() {
        return currY;
    }

    double getYPlusSize() {
        return currY + SNAKE_SIZE/2;
    }

    double getYMinusSize() {
        return currY - SNAKE_SIZE/2;
    }

    void setY(double y) {
        currY = y;
    }

    int getGrowth() {
        return growth;
    }

    void setGrowth(int growth) {
        this.growth = growth;
    }

    void eat() {
        growth++;
    }

    double getSnakeSize() {
        return SNAKE_SIZE;
    }
}
