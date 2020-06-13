package com.mycompany.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.concurrent.TimeUnit;

public class StartGame extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final double POSITIVE_SPEED = 4;
    private final double NEGATIVE_SPEED = -4;
    private final int GAME_REDRAW_SPEED = 10;
    private final int GAME_TIMER_ONE_SECOND = 1000;
    private final int MAX = 4;
    private final int MIN = 1;
    private Snake snakePiece;
    private Snake snakeBody[];
    private Food food;
    static Timer movementTimer;
    static Timer playTimer;
    double velx, vely;
    int counter = 0;

    public StartGame(float frameWidth, float frameHeight) {
        startGameSetup(frameWidth, frameHeight);
        // startGame();
        // System.out.println("new GAME");
        // System.out.println("velx: " + velx);
        // System.out.println("vely: " + vely);
    }

    private void startGameSetup(float frameWidth, float frameHeight) {
        snakePiece = new Snake(frameWidth, frameHeight, true);
        food = new Food(frameWidth, frameHeight);
        int maxSnakeLength = (int) ((frameWidth * frameHeight) / (snakePiece.getSnakeSize() * 2));
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
        double snakeSize = snakePiece.getSnakeSize();
        snakeHeadDirChange(0, snakeSize);
        for (int i = 0; i < snakeBody.length; i++) {
            if (snakeBody[i] == null) {
                // System.out.println("break");
                break;
            }
            // System.out.println("redraw");
            g2.fill(new Ellipse2D.Double(snakeBody[i].getX(), snakeBody[i].getY(), snakePiece.SNAKE_SIZE,
                    snakePiece.SNAKE_SIZE));
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        double snakeSize = snakePiece.getSnakeSize();
        wallCheck();
        eatCheck();
        try {
            repaint();
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        for (int i = snakePiece.getGrowth(); i > 0; i--) {
                snakeBodyGrow(i, snakeSize);
        }
    }

    // TODO: fix snake spacing... or call it the caterpillar game :D
    private void snakeBodyGrow(int i, double snakeSize) {
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

    private void snakeHeadDirChange(int i, double snakeSize) {
        // System.out.println("Just 1: " + i);
        if (velx > 0) {
            snakeBody[i].setX(snakePiece.getX() + velx);
            snakeBody[i].setY(snakePiece.getY());
            // System.out.println("velx > 0");
            // snakeBody[i].setPrevX((snakePiece.getX() - snakePiece.getSnakeSize()) + velx);
            // snakeBody[i].setPrevY(snakePiece.getY() + velx);
            // snakeBody[i].setX((food.getX() - snakePiece.getSnakeSize()*i) + velx);
            // snakeBody[i].setY(food.getY() + vely);
        } else if (velx < 0) {
            // System.out.println("velx < 0");
            snakeBody[i].setX((snakePiece.getX()) + velx);
            snakeBody[i].setY(snakePiece.getY());
        } else if (vely > 0) {
            // System.out.println("vely > 0");
            snakeBody[i].setX(snakePiece.getX());
            snakeBody[i].setY((snakePiece.getY()) + vely);
        } else if (vely < 0) {
            // System.out.println("vely < 0");
            snakeBody[i].setX(snakePiece.getX());
            snakeBody[i].setY((snakePiece.getY()) + vely);
        }
    }

    private void foodCheck(final Graphics2D g2) {
        food.spawnFood(g2, food.foodFlag);
        if (!food.foodFlag) {
            toggleFlag();
            // System.out.println("toggle");
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
            // System.out.println("eat");
            snakePiece.eat();
            // snakeBody[snakePiece.getGrowth()] = new Snake(snakePiece.getX(), snakePiece.getY(), food.getX(), food.getY());
            // snakeBody[snakePiece.getGrowth()] = new Snake(snakePiece.getX(), snakePiece.getY());
            snakeBody[snakePiece.getGrowth()] = new Snake(snakeBody[snakePiece.getGrowth()-1].getX2(), snakeBody[snakePiece.getGrowth()-1].getY2());
            System.out.println("new snake body GROWTH: " + snakePiece.getGrowth());
            toggleFlag();
            GameFrame.increaseScore();
        }
    }

    private boolean checkRight() {
        return snakePiece.getX() > food.getX() && snakePiece.getX() < food.getX() + food.getSize()
            && snakePiece.getY() > food.getY() && snakePiece.getY() < food.getY() + food.getSize();
    }

    private boolean checkLeft() {
        return snakePiece.getX() + snakePiece.getSnakeSize() > food.getX() && snakePiece.getX() < food.getX() + food.getSize()
            && snakePiece.getY() + snakePiece.getSnakeSize() > food.getY() && snakePiece.getY() < food.getY() + food.getSize();
    }

    void toggleFlag() {
        food.foodFlag = !food.foodFlag;
    }

    private void setStartDirection() {
        final int dir = (int) (Math.random() * (MAX - MIN + 1) + MIN);
        // System.out.println(dir);
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

/**
if (velx > 0) {
    System.out.println("new snake body");
    // System.out.println("velx > 0");
    snakeBody[snakePiece.getGrowth()] = new Snake(snakePiece.getX() - snakePiece.SNAKE_SIZE, snakePiece.getY(),
            snakeBody[snakePiece.getGrowth() - 1].getX(), snakeBody[snakePiece.getGrowth() - 1].getY());
    // snakeBody[i].setX((snakeBody[i-1].getX() - snakePiece.getSnakeSize()*i) + velx);
    // snakeBody[i].setY(snakeBody[i-1].getY() + vely);
} else if (velx < 0) {
    System.out.println("new snake body");
    // System.out.println("velx < 0");
    snakeBody[snakePiece.getGrowth()] = new Snake(snakePiece.getX() + snakePiece.SNAKE_SIZE, snakePiece.getY(),
    snakeBody[snakePiece.getGrowth() - 1].getX(), snakeBody[snakePiece.getGrowth() - 1].getY());

    // snakeBody[i].setX((snakeBody[i-1].getX() + snakePiece.getSnakeSize()*i) + velx);
    // snakeBody[i].setY(snakeBody[i-1].getY() + vely);
} else if (vely > 0) {
    System.out.println("new snake body");
    // System.out.println("vely > 0");
    snakeBody[snakePiece.getGrowth()] = new Snake(snakePiece.getX(), snakePiece.getY() - snakePiece.SNAKE_SIZE,
    snakeBody[snakePiece.getGrowth() - 1].getX(), snakeBody[snakePiece.getGrowth() - 1].getY());
    // snakeBody[i].setX(snakeBody[i-1].getX() + velx);
    // snakeBody[i].setY((snakeBody[i-1].getY() - snakePiece.getSnakeSize()*i) + vely);
} else if (vely < 0) {
    System.out.println("new snake body");
    // System.out.println("vely < 0");
    snakeBody[snakePiece.getGrowth()] = new Snake(snakePiece.getX(), snakePiece.getY() + snakePiece.SNAKE_SIZE,
    snakeBody[snakePiece.getGrowth() - 1].getX(), snakeBody[snakePiece.getGrowth() - 1].getY());
    // snakeBody[i].setX(snakeBody[i-1].getX()  + velx);
    // snakeBody[i].setY((snakeBody[i-1].getY() + snakePiece.getSnakeSize()*i) + vely);
} 
*/