package com.mycompany.snakegame;
/**
 * This will be a beginners personal project to the 1997 Nokia Snake game
 */

public class Snake {
    final double SNAKE_SIZE = 20;
    private double currX, currY;
    private double velX, velY;
    private int growth;

    public Snake() {
        currX = 0;
        currY = 0;
        velX = 0;
        velY = 0;
        growth = 0;
    }

    public Snake(double currX, double currY, boolean center) {
        this.currX = currX/2;
        this.currY = currY/2;
        velX = 0;
        velY = 0;
        growth = 0;
    }
    
    public Snake(double currX, double currY, double velX, double velY) {
        this.currX = currX;
        this.currY = currX;
        this.velX = velX;
        this.velY = velY;
        growth = 0;
    }
    

    double getX() {
        return currX;
    }

    double getXPlusHalfSize() {
        return currX + SNAKE_SIZE/2;
    }

    double getXPlusSize() {
        return currX + SNAKE_SIZE;
    }

    double getXMinusHalfSize() {
        return currX - SNAKE_SIZE/2;
    }

    double getXMinusSize() {
        return currX - SNAKE_SIZE;
    }

    void setX(double x) {
        currX = x;
    }

    double getY() {
        return currY;
    }

    double getYPlusHalfSize() {
        return currY + SNAKE_SIZE/2;
    }

    double getYPlusSize() {
        return currY + SNAKE_SIZE;
    }

    double getYMinusHalfSize() {
        return currY - SNAKE_SIZE/2;
    }
    double getYMinusSize() {
        return currY - SNAKE_SIZE;
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

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }
}
