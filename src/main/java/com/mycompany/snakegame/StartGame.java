package com.mycompany.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.concurrent.TimeUnit;

public class StartGame extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final double POSITIVE_SPEED = 6;
    private final double NEGATIVE_SPEED = -6;
    private final int GAME_REDRAW_SPEED = 15;
    private final int GAME_TIMER_ONE_SECOND = 1000;
    private final int MAX = 4;
    private final int MIN = 1;
    private int maxSnakeLength;
    private Snake snakePiece;
    private Snake snakeBody[];
    private Food food;
    static Timer movementTimer;
    static Timer playTimer;
    double velx, vely;
    int counter = 0;

    public StartGame(float frameWidth, float frameHeight) {
        startGame(frameWidth, frameHeight);
    }

    private void startGame(float frameWidth, float frameHeight) {
        snakePiece = new Snake(frameWidth, frameHeight, true);
        food = new Food(frameWidth, frameHeight);
        maxSnakeLength = (int) ((frameWidth * frameHeight) / (snakePiece.getSnakeSize() * 2));
        snakeBody = new Snake[maxSnakeLength];
        snakeBody[0] = snakePiece;
        velx = 0;
        vely = 0;
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
            g2.fill(new Ellipse2D.Double(snakeBody[i].getX(), snakeBody[i].getY(), snakePiece.SNAKE_SIZE,
                snakePiece.SNAKE_SIZE));
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        repaint();
        for (int i = snakePiece.getGrowth(); i >= 0; i--) {
            if (i == 0) {
                snakeHeadDirChange();
                break;
            }
            snakeBodyGrow(i);
        }
    }

    // TODO: fix snake spacing... or call it the caterpillar game :D
    private void snakeBodyGrow(int i) {
        if (velx > 0) {
            snakeBody[i].setX(snakeBody[i-1].getX());
            snakeBody[i].setY(snakeBody[i-1].getY());
        } else if (velx < 0) {
            snakeBody[i].setX(snakeBody[i-1].getX());
            snakeBody[i].setY(snakeBody[i-1].getY());
        } else if (vely > 0) {
            snakeBody[i].setX(snakeBody[i-1].getX());
            snakeBody[i].setY(snakeBody[i-1].getY());
        } else if (vely < 0) {
            snakeBody[i].setX(snakeBody[i-1].getX());
            snakeBody[i].setY(snakeBody[i-1].getY());
        }
    }

    private void snakeHeadDirChange() {
        if (velx > 0) {
            snakePiece.setX(snakePiece.getX() + velx);
            snakePiece.setY(snakePiece.getY());
        } else if (velx < 0) {
            snakePiece.setX(snakePiece.getX() + velx);
            snakePiece.setY(snakePiece.getY());
        } else if (vely > 0) {
            snakePiece.setX(snakePiece.getX());
            snakePiece.setY(snakePiece.getY() + vely);
        } else if (vely < 0) {
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
            snakeBody[snakePiece.getGrowth()] = new Snake(-25, -25);
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
        int dir = (int) (Math.random() * (MAX - MIN + 1) + MIN);
        if (dir == 1) {
            vely = NEGATIVE_SPEED;
            velx = 0;
        } else if (dir == 2) {
            vely = 0;
            velx = POSITIVE_SPEED;
        } else if (dir == 3) {
            vely = POSITIVE_SPEED;
            velx = 0;
        } else {
            vely = 0;
            velx = NEGATIVE_SPEED;
        }
    }

    private void up() {
        if (vely <= 0) {
            vely = NEGATIVE_SPEED;
            velx = 0;
        }
    }

    private void down() {
        if (vely >= 0) {
            vely = POSITIVE_SPEED;
            velx = 0;
        }
    }

    private void left() {
        if (velx <= 0) {
            vely = 0;
            velx = NEGATIVE_SPEED;
        }
    }

    private void right() {
        if (velx >= 0) {
            vely = 0;
            velx = POSITIVE_SPEED;
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