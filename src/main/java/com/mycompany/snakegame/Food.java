package com.mycompany.snakegame;

import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class Food {
    
    /**
     *
     */
    Color foodColor = Color.BLACK;
    private final int FOOD_SIZE = 20;
    boolean foodFlag;
    int x, y;
    float constraintX, constraintY;

    public Food(float constraintX, float constraintY) {
        foodFlag = false;
        x = 0;
        y = 0;
        this.constraintX = constraintX;
        this.constraintY = constraintY;
    }

    public void spawnFood(final Graphics g, boolean foodFlag) {
        final Graphics2D g2 = (Graphics2D) g;
        if (!foodFlag) {
            Random rand = new Random();
            x = rand.nextInt((int) constraintX);
            y = rand.nextInt((int) constraintY);
        }
        g2.setColor(foodColor);
        g2.fillRect(x, y, FOOD_SIZE, FOOD_SIZE);
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getSize() {
        return FOOD_SIZE;
    }
}