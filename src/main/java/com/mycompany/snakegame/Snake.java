package com.mycompany.snakegame;
/**
 * This will be a beginners personal project to the 1997 Nokia Snake game
 */

public class Snake {
    final double SNAKE_SIZE = 20;
    final double SIZE_ALIGN = 15;
    private double currX, currY;
    private double velx, vely;
    private int direction;
    private int tail;

    public Snake() {
        this.currX = 0;
        this.currY = 0;
        this.velx = 0;
        this.vely = 0;
        this.direction = 0;
        tail = 0;
    }

    public Snake(double currX, double currY) {
        this.currX = currX/2;
        this.currY = currY/2;
        this.velx = 0;
        this.vely = 0;
        this.direction = 0;
        tail = 0;
    }
    
    public Snake(double currX, double currY, int direction) {
        this.currX = currX;
        this.currY = currX;
        this.velx = 0;
        this.vely = 0;
        this.direction = direction;
        tail = 0;
    }
    

    public static void main(String[] args) {
        new GameFrame();
    }

    double getX() {
        return currX;
    }

    double getX2() {
        return currX + SIZE_ALIGN;
    }

    double getX3() {
        return currX - SIZE_ALIGN;
    }

    void setX(double x) {
        currX = x;
    }

    double getY() {
        return currY;
    }

    double getY2() {
        return currY + SIZE_ALIGN;
    }

    double getY3() {
        return currY - SIZE_ALIGN;
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
        tail++;
    }

    double getSnakeSize() {
        return SNAKE_SIZE;
    }

    public double getVelx() {
        return velx;
    }

    public void setVelx(double velx) {
        this.velx = velx;
    }

    public double getVely() {
        return vely;
    }

    public void setVely(double vely) {
        this.vely = vely;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
