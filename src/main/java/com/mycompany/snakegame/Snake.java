package com.mycompany.snakegame;
/**
 * This will be a beginners personal project to the 1997 Nokia Snake game
 */

public class Snake {
    final int SNAKE_SIZE = 20;
    private double currX, currY;
    private double prevX, prevY;
    private int growth;

    public Snake(double currX, double currY) {
        this.currX = currX/2;
        this.currY = currY/2;
        prevX = currX/2;
        prevY = currX/2;
        growth = 0;
    }
    
    public Snake(double currX, double currY, double prevX, double prevY) {
        this.currX = currX;
        this.currY = currX;
        this.prevX = prevX;
        this.prevY = prevY;
        growth = 0;
    }

    public static void main(String[] args) {
        new GameFrame();
    }

    double getX() {
        return currX;
    }

    double getX2() {
        return currX + SNAKE_SIZE;
    }

    void setX(double x) {
        currX = x;
    }

    double getY() {
        return currY;
    }

    double getY2() {
        return currY + SNAKE_SIZE;
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
        // TODO: make this into function to add length to the snake
        growth++;
    }

    int getSnakeSize() {
        return SNAKE_SIZE;
    }
}
