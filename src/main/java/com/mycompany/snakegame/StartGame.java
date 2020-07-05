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
    private  double POSITIVE_SPEED = 20;   // Higher is faster
    private  double NEGATIVE_SPEED = -20;  // Higher is faster
    private final int GAME_REDRAW_SPEED = 50;   // Lower is faster, but will cause less flicker
    private final int GAME_TIMER_ONE_SECOND = 1000;
    private final int MAX = 4;
    private final int MIN = 1;
    private int maxSnakeLength;
    private Snake snakePiece;
    private Snake snakeBody[];
    private Food food;
    static Timer movementTimer;
    static Timer playTimer;

    
    public StartGame(float frameWidth, float frameHeight) {
        startGame(frameWidth, frameHeight);
    }

    private void startGame(float frameWidth, float frameHeight) {
        snakePiece = new Snake(GameFrame.getAdjustedWidthForSnakeStart(), GameFrame.getAdjustedHeightForSnakeStart(), true);
        food = new Food();
        maxSnakeLength = (int) ((frameWidth * frameHeight) / (snakePiece.getSnakeSize() * 2));
        snakeBody = new Snake[maxSnakeLength];
        snakeBody[0] = snakePiece;
        setStartDirection();
        startTimers();
        addKeyListener(this);
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

    @Override
    public void paintComponent(final Graphics g) {
        requestFocus(true);
        final Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        foodCheck(g2);
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(snakeBody[0].getX(), snakeBody[0].getY(), snakePiece.SNAKE_SIZE,
            snakePiece.SNAKE_SIZE));
        for (int i = snakePiece.getGrowth(); i > 0; i--) {
            g2.setColor(Color.BLACK);
            g2.fill(new Ellipse2D.Double(snakeBody[i].getX(), snakeBody[i].getY(), snakePiece.SNAKE_SIZE,
                    snakePiece.SNAKE_SIZE));
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        for (int i = snakePiece.getGrowth(); i >= 0; i--) {
            repaint();
            bodyCheck();
            if (i == 0) {
                eatCheck();
                snakeHeadDirChange();
                wallCheck();
                break;
            }
            snakeBodyDirChange(i);
        }
    }

    private void snakeBodyDirChange(int i) {
        if (snakeBody[i].getVelX() != snakeBody[i-1].getVelX() || snakeBody[i].getVelY() != snakeBody[i-1].getVelY()) {
            snakeBody[i].setX(snakeBody[i-1].getX());
            snakeBody[i].setY(snakeBody[i-1].getY());
            snakeBody[i].setVelX(snakeBody[i-1].getVelX());
            snakeBody[i].setVelY(snakeBody[i-1].getVelY());
        } else {
            if (snakeBody[i].getVelY() < 0) {
                snakeBody[i].setX(snakeBody[i-1].getX());
                snakeBody[i].setY(snakeBody[i-1].getYPlusSize() + snakeBody[i-1].getVelY());
            } else if (snakeBody[i].getVelY() > 0) {
                snakeBody[i].setX(snakeBody[i-1].getX());
                snakeBody[i].setY(snakeBody[i-1].getYMinusSize() + snakeBody[i-1].getVelY());
            } else if (snakeBody[i].getVelX() < 0) {
                snakeBody[i].setX(snakeBody[i-1].getXPlusSize() + snakeBody[i-1].getVelX());
                snakeBody[i].setY(snakeBody[i-1].getY());
            } else if (snakeBody[i].getVelX() > 0) {
                snakeBody[i].setX(snakeBody[i-1].getXMinusSize() + snakeBody[i-1].getVelX());
                snakeBody[i].setY(snakeBody[i-1].getY());
            }
        }
    }

    private void snakeHeadDirChange() {
        snakePiece.setX(snakePiece.getX() + snakePiece.getVelX());
        snakePiece.setY(snakePiece.getY() + snakePiece.getVelY());
    }

    private void foodCheck(final Graphics2D g2) {
        food.spawnFood(g2, food.foodFlag);
        if (!food.foodFlag) {
            toggleFlag();
        }
    }

    void toggleFlag() {
        food.foodFlag = !food.foodFlag;
    }

    private void wallCheck() {
        if (snakePiece.getX() < GameFrame.getAdjustedWidthForLeftWall() ||
                snakePiece.getXPlusSize() > GameFrame.getAdjustedWidthForRightWall() ||
                snakePiece.getY() < GameFrame.getAdjustedHeightForTopWall() ||
                snakePiece.getYPlusSize() > GameFrame.getAdjustedHeightForBotWall()) {
            endRound();
        }
    }

    private void bodyCheck() {
        double headX = snakePiece.getX();
        double headY = snakePiece.getY();
        for (int i = snakePiece.getGrowth(); i > 2; i--) {
            if(headX >= snakeBody[i].getX()-1 && headX <= snakeBody[i].getXPlusSize()-1 && 
                    headY >= snakeBody[i].getY()-1 && headY <= snakeBody[i].getYPlusSize()-1) {
                endRound();
            }
        }
    }

    private void eatCheck() {
        if(checkRight() || checkLeft()) {
            snakePiece.eat();
            int tail = snakePiece.getGrowth();
            snakeBody[tail] = new Snake(0, 0, snakeBody[tail-1].getVelX(), snakeBody[tail-1].getVelY());
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

    private void endRound() {
        movementTimer.stop();
        playTimer.stop();
        GameFrame.displayGameOver();
    }

    private void setStartDirection() {
        int dir = (int) (Math.random() * (MAX - MIN + 1) + MIN);
        if (dir == 1) { //  UP
            snakePiece.setVelY(NEGATIVE_SPEED);
            snakePiece.setVelX(0);
        } else if (dir == 2) {  //  DOWN
            snakePiece.setVelY(POSITIVE_SPEED);
            snakePiece.setVelX(0);
        } else if (dir == 3){   // LEFT
            snakePiece.setVelY(0);
            snakePiece.setVelX(NEGATIVE_SPEED);
        } else {    // RIGHT
            snakePiece.setVelY(0);
            snakePiece.setVelX(POSITIVE_SPEED);
        } 
    }

    private void up() {
        if (snakePiece.getVelY() <= 0) {
            snakePiece.setVelY(NEGATIVE_SPEED);
            snakePiece.setVelX(0);
        }
    }

    private void down() {
        if (snakePiece.getVelY() >= 0) {
            snakePiece.setVelY(POSITIVE_SPEED);
            snakePiece.setVelX(0);
        }
    }

    private void left() {
        if (snakePiece.getVelX() <= 0) {
            snakePiece.setVelY(0);
            snakePiece.setVelX(NEGATIVE_SPEED);
        }
    }

    private void right() {
        if (snakePiece.getVelX() >= 0) {
            snakePiece.setVelY(0);
            snakePiece.setVelX(POSITIVE_SPEED);
        }
    }

    private void space() {
        movementTimer.setDelay(100);
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
        if (code == KeyEvent.VK_SPACE) {
            space();
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }
}