package com.mycompany.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class StartGame extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final double POSITIVE_SPEED = 3;    // Higher is faster
    private final double NEGATIVE_SPEED = -3;   // Higher is faster 
    private final int GAME_REDRAW_SPEED = 12;   // Higher is slower
    private final int GAME_TIMER_ONE_SECOND = 1000;
    private Snake snakePiece;
    private Snake snakeBody[];
    private Food food;
    static Timer movementTimer;
    static Timer playTimer;
    int directionFlag;
    int counter = 0;

    public StartGame(float frameWidth, float frameHeight) {
        startGame(frameWidth, frameHeight);
    }

    private void startGame(float frameWidth, float frameHeight) {
        snakePiece = new Snake(frameWidth, frameHeight);
        food = new Food(frameWidth, frameHeight);
        int maxSnakeLength = (int) ((frameWidth * frameHeight) / (snakePiece.getSnakeSize() * 2));
        snakeBody = new Snake[maxSnakeLength];
        snakeBody[0] = snakePiece;
        startTimers();
        addKeyListener(this);
        setStartDirection();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void startTimers() {
        movementTimer = new Timer(GAME_REDRAW_SPEED, this);
        movementTimer.start();
        playTimer = new Timer(GAME_TIMER_ONE_SECOND, playTime);
        playTimer.start();
    }

    ActionListener playTime = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameFrame.increaseTime();
        }
    };

    public void paintComponent(final Graphics g) {
        requestFocus(true);
        final Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        foodCheck(g2);
        wallCheck();
        eatCheck();
        for (int i = 0; i <= snakePiece.getGrowth(); i++) {
            g2.fill(new Ellipse2D.Double(snakeBody[i].getX(), snakeBody[i].getY(), 
                snakePiece.SNAKE_SIZE, snakePiece.SNAKE_SIZE));
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        for (int i = snakePiece.getGrowth(); i >= 0; i--) {
            if (i != 0) {
                snakeBodyDirChange(i);
            } else {
                snakeHeadDirChange();
                repaint();
            }
        }
    }

    // TODO: fix snake spacing... or call it the caterpillar game :D
    private void snakeBodyDirChange(int i) {
        double velx = snakeBody[i-1].getVelx();
        double vely = snakeBody[i-1].getVely();
        // if (snakeBody[i].getDirection() == snakeBody[i-1].getDirection()) {
            // if (velx > 0) {
            //     snakeBody[i].setX(snakeBody[i-1].getX3());
            //     snakeBody[i].setY(snakeBody[i-1].getY());
            // } else if (velx < 0) {
            //     snakeBody[i].setX(snakeBody[i-1].getX2());
            //     snakeBody[i].setY(snakeBody[i-1].getY());
            // } else if (vely > 0) {
            //     snakeBody[i].setX(snakeBody[i-1].getX());
            //     snakeBody[i].setY(snakeBody[i-1].getY3());
            // } else if (vely < 0) {
            //     snakeBody[i].setX(snakeBody[i-1].getX());
            //     snakeBody[i].setY(snakeBody[i-1].getY2());
            // }
        // } else {
            snakeBody[i].setX(snakeBody[i-1].getX());
            snakeBody[i].setY(snakeBody[i-1].getY());
            snakeBody[i].setVelx(velx);
            snakeBody[i].setVely(vely);
            // snakeBody[i].setDirection(snakeBody[i-1].getDirection());
        // }
    }

    private void snakeHeadDirChange() {
        double velx = snakePiece.getVelx();
        double vely = snakePiece.getVely();
        if (velx > 0) {
            snakePiece.setDirection(2);
            snakePiece.setX(snakePiece.getX() + velx);
            snakePiece.setY(snakePiece.getY());
        } else if (velx < 0) {
            snakePiece.setDirection(4);
            snakePiece.setX(snakePiece.getX() + velx);
            snakePiece.setY(snakePiece.getY());
        } else if (vely > 0) {
            snakePiece.setDirection(3);
            snakePiece.setX(snakePiece.getX());
            snakePiece.setY(snakePiece.getY() + vely);
        } else if (vely < 0) {
            snakePiece.setDirection(1);
            snakePiece.setX(snakePiece.getX());
            snakePiece.setY(snakePiece.getY() + vely);
        }
    }

    private void foodCheck(final Graphics2D g2) {
        food.spawnFood(g2, food.foodFlag);
        if (!food.foodFlag) {
            toggleFlag();
        }
    }

    private void wallCheck() {
        if (snakePiece.getX() < 0 || snakePiece.getX() >= GameFrame.FRAME_WIDTH - 35
                || snakePiece.getY() < 0 || snakePiece.getY() > GameFrame.FRAME_HEIGHT - 110) {
            movementTimer.stop();
            playTimer.stop();
        }
    }

    private void eatCheck() {
        if(checkRight() || checkLeft()) {
            snakePiece.eat();
            snakeBody[snakePiece.getGrowth()] = new Snake(-25, -25, snakeBody[snakePiece.getGrowth()-1].getDirection());
            toggleFlag();
            GameFrame.increaseScore();
        }
    }

    private boolean checkRight() {
        double currFoodLocX = food.getX();
        double currFoodLocY = food.getY();
        double foodSize = food.getSize();
        return snakePiece.getX() > currFoodLocX && snakePiece.getX() < currFoodLocX + foodSize
            && snakePiece.getY() > currFoodLocY && snakePiece.getY() < currFoodLocY + foodSize;
    }

    private boolean checkLeft() {
        double currFoodLocX = food.getX();
        double currFoodLocY = food.getY();
        double foodSize = food.getSize();
        return snakePiece.getX() + snakePiece.getSnakeSize() > currFoodLocX && snakePiece.getX() < currFoodLocX + foodSize
            && snakePiece.getY() + snakePiece.getSnakeSize() > currFoodLocY && snakePiece.getY() < currFoodLocY + foodSize;
    }

    void toggleFlag() {
        food.foodFlag = !food.foodFlag;
    }

    private void setStartDirection() {
        final int MAX = 4;
        final int MIN = 1;
        directionFlag = (int) (Math.random() * (MAX - MIN + 1) + MIN);
        if (directionFlag == 1) {
            snakePiece.setVely(NEGATIVE_SPEED);
            snakePiece.setVelx(0);
            // vely = NEGATIVE_SPEED;
            // velx = 0;
        } else if (directionFlag == 2) {
            snakePiece.setVely(0);
            snakePiece.setVelx(POSITIVE_SPEED);
            // vely = 0;
            // velx = POSITIVE_SPEED;
        } else if (directionFlag == 3) {
            snakePiece.setVely(POSITIVE_SPEED);
            snakePiece.setVelx(0);
            // vely = POSITIVE_SPEED;
            // velx = 0;
        } else {
            snakePiece.setVely(0);
            snakePiece.setVelx(NEGATIVE_SPEED);
            // vely = 0;
            // velx = NEGATIVE_SPEED;
        }
        snakePiece.setDirection(directionFlag);
        System.out.println("directionFlag: " + directionFlag);
    }

    private void up() {
        // if (vely <= 0) {
        //     vely = NEGATIVE_SPEED;
        //     velx = 0;
        // }
        if (snakePiece.getVely() <= 0) {
            snakePiece.setVely(NEGATIVE_SPEED);
            snakePiece.setVelx(0);
            snakePiece.setDirection(1);
        }
    }

    private void down() {
        // if (vely >= 0) {
        //     vely = POSITIVE_SPEED;
        //     velx = 0;
        // }
        if (snakePiece.getVely() >= 0) {
            snakePiece.setVely(POSITIVE_SPEED);
            snakePiece.setVelx(0);
            snakePiece.setDirection(3);
        }
    }

    private void left() {
        // if (velx <= 0) {
        //     vely = 0;
        //     velx = NEGATIVE_SPEED;
        // }
        if (snakePiece.getVelx() <= 0) {
            snakePiece.setVely(0);
            snakePiece.setVelx(NEGATIVE_SPEED);
            snakePiece.setDirection(4);
        }
    }

    private void right() {
        // if (velx >= 0) {
        //     vely = 0;
        //     velx = POSITIVE_SPEED;
        // }
        if (snakePiece.getVelx() >= 0) {
            snakePiece.setVely(0);
            snakePiece.setVelx(POSITIVE_SPEED);
            snakePiece.setDirection(2);
        }
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            up();
        }
        if (code == KeyEvent.VK_DOWN) {
            down();
        }
        if (code == KeyEvent.VK_LEFT) {
            left();
        }
        if (code == KeyEvent.VK_RIGHT) {
            right();
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }
}