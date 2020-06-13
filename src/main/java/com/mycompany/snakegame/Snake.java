package com.mycompany.snakegame;
/**
 * This will be a beginners personal project to the 1997 Nokia Snake game
 */

public class Snake {
    final double SNAKE_SIZE = 20;
    private double currX, currY;
    private double prevX, prevY;
    private int tail;

    public Snake() {
        this.currX = 0;
        this.currY = 0;
        this.prevX = 0;
        this.prevY = 0;
        tail = 0;
    }

    public Snake(double currX, double currY, boolean center) {
        this.currX = currX/2;
        this.currY = currY/2;
        prevX = 0;
        prevY = 0;
        tail = 0;
    }
    
    public Snake(double currX, double currY) {
        this.currX = currX;
        this.currY = currX;
        this.prevX = 0;
        this.prevY = 0;
        tail = 0;
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
        return tail;
    }

    void setGrowth(int growth) {
        this.tail = growth;
    }

    void eat() {
        // TODO: make this into function to add length to the snake
        tail++;
    }

    double getSnakeSize() {
        return SNAKE_SIZE;
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }
}
