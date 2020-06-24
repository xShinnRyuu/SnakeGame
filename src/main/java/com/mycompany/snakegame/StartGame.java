package com.mycompany.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class StartGame extends JPanel implements ActionListener, KeyListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final double POSITIVE_SPEED = 20;   // Higher is faster
    private final double NEGATIVE_SPEED = -20;  // Higher is faster
    private final int GAME_REDRAW_SPEED = 40;   // Lower is faster, but will cause less flicker
    private final int GAME_TIMER_ONE_SECOND = 1000;
    private final int MAX = 4;
    private final int MIN = 1;
    private final int GAME_WIDTH = 890;
    private int maxSnakeLength;
    private Snake snakePiece;
    private Snake snakeBody[];
    private Food food;
    static Timer movementTimer;
    static Timer playTimer;
    double velx, vely;

    public static void main(String[] args) {
        new GameFrame();
    }
    
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
        for (int i = snakePiece.getGrowth(); i >= 0; i--) {
            g2.fill(new Ellipse2D.Double(snakeBody[i].getX(), snakeBody[i].getY(), snakePiece.SNAKE_SIZE,
                snakePiece.SNAKE_SIZE));
                // g2.fillRect(830-10, 840-10, 10, 10);
                g2.fillRect(900, 0, 10, 1000);
                // g2.fillRect(0, 840-10, 10, 10);
            // g2.fill(new Ellipse2D.Double(snakePiece.getX(), snakePiece.getY(), snakePiece.SNAKE_SIZE,
            // snakePiece.SNAKE_SIZE));
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        repaint();
        for (int i = snakePiece.getGrowth(); i >= 0; i--) {
            if (i == 0) {
                snakeHeadDirChange();
                wallCheck();
                bodyCheck();
                eatCheck();
                break;
            }
            snakeBodyGrow(i);
        }
    }

    private void snakeBodyGrow(int i) {
        snakeBody[i].setX(snakeBody[i-1].getX());
        snakeBody[i].setY(snakeBody[i-1].getY());
    }

    private void snakeHeadDirChange() {
        if (vely < 0 || vely > 0) {
            snakePiece.setX(snakePiece.getX());
            snakePiece.setY(snakePiece.getY() + vely);
        } else if (velx < 0 || velx > 0) {
            snakePiece.setX(snakePiece.getX() + velx);
            snakePiece.setY(snakePiece.getY());
        } 
    }

    private void foodCheck(final Graphics2D g2) {
        food.spawnFood(g2, food.foodFlag);
        if (!food.foodFlag) {
            toggleFlag();
        }
    }

    private void wallCheck() {
        if (snakePiece.getX() <= 0 || snakePiece.getXPlusSize() >= GAME_WIDTH
                || snakePiece.getY() <= 0 || snakePiece.getY() >= GameFrame.FRAME_HEIGHT) {
            System.out.println("X: " + snakePiece.getX());
            System.out.println("Y: " + snakePiece.getY());
            System.out.println("GameFrame.FRAME_WIDTH: " + GameFrame.FRAME_WIDTH);
            System.out.println("GameFrame.FRAME_HEIGHT: " + GameFrame.FRAME_HEIGHT);
            System.out.println();
            movementTimer.stop();
            playTimer.stop();
        }
    }

    // TODO: fix the body check, the if condition is wrong
    private void bodyCheck() {
        double headX = snakePiece.getX();
        double headY = snakePiece.getY();
        for (int i = 0; i < snakePiece.getGrowth(); i++) {
            if(headX >= snakeBody[i].getX() && headX <= snakeBody[i].getXPlusSize() &&
                headY <= snakeBody[i].getY() && headY >= snakeBody[i].getYPlusSize()) {
                    movementTimer.stop();
                    playTimer.stop();
            }
        }
    }

    private void eatCheck() {
        if(checkRight() || checkLeft()) {
            snakePiece.eat();
            int tail = snakePiece.getGrowth();
            snakeBody[tail] = new Snake(-25, -25);
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
        if (dir == 1) { //  UP
            vely = NEGATIVE_SPEED;
            velx = 0;
        } else if (dir == 2) {  //  DOWN
            vely = POSITIVE_SPEED;
            velx = 0;
        } else if (dir == 3){   // LEFT
            vely = 0;
            velx = NEGATIVE_SPEED;
        } else {    // RIGHT
            vely = 0;
            velx = POSITIVE_SPEED;
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