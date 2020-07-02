package com.mycompany.snakegame;

import java.awt.*;
import java.util.Random;

public class Food {
    
    /**
     *
     */
    Color foodColor = Color.BLACK;
    private final int FOOD_SIZE = 20;
    boolean foodFlag;
    int x, y;
    int constraintXLeft, constraintXRight, constraintYTop, constraintYBot;

    public Food() {
        foodFlag = false;
        x = 0;
        y = 0;
        this.constraintXLeft = GameFrame.getAdjustedWidthForLeftWall();
        this.constraintXRight = GameFrame.getAdjustedWidthForRightWall();
        this.constraintYTop = GameFrame.getAdjustedHeightForTopWall();
        this.constraintYBot = GameFrame.getAdjustedHeightForBotWall();
    }

    public void spawnFood(final Graphics g, boolean foodFlag) {
        final Graphics2D g2 = (Graphics2D) g;
        if (!foodFlag) {
            Random rand = new Random();
            x = rand.nextInt(constraintXRight - constraintXLeft - FOOD_SIZE) + constraintXLeft;
            y = rand.nextInt((constraintYBot - 10) - constraintYTop - FOOD_SIZE) + constraintYTop;
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