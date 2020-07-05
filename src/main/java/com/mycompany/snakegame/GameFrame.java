package com.mycompany.snakegame;

import javax.swing.BorderFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

class GameFrame extends JFrame implements ActionListener {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static final int FRAME_WIDTH = 874;
    static final int FRAME_HEIGHT = 737;
    static final double STATS_PANEL = 40;
    final int BORDER_HORIZONTAL = 245;
    final int BORDER_VERTICAL = 200;
    final int VERTICAL_ALIGNMENT = 110;
    final int HORIZONTAL_ALIGNMENT = 20;
    static int score = 0;
    static int time = 0;
    static final int BORDER_SIZE = 20;
    private JFrame gameFrame;
    private JPanel statsPanel;
    private JPanel menuPanel;
    private static JPanel currGamePanel;
    static JLabel timeLabel;
    static JLabel scoreLabel;
    static JLabel gameOver;
    JButton resetButton;
    JButton exitButton;
    static Insets insets;
    TimerTask task;

    // TODO: ADD a border
    GameFrame() {
        gameFrame = new JFrame();
        setGameFrameParams();
        createMenuPanel();
        gameOver = new JLabel();
        insets = gameFrame.getInsets();
    }

    // set game frame parameters
    private void setGameFrameParams() {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        gameFrame.setLocation(size.width / 2 - (int) FRAME_HEIGHT / 2, size.height / 2 - (int) FRAME_WIDTH / 2);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setTitle("Sneks!");
        gameFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
        gameFrame.pack();
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_VERTICAL, BORDER_HORIZONTAL, BORDER_VERTICAL,
                BORDER_HORIZONTAL));
        menuPanel.setLayout(new GridLayout(1, 0));
        menuPanel.setBackground(new Color(152, 16, 25));
        JPanel buttonPanel = createButtonPanel();
        menuPanel.add(buttonPanel);
        gameFrame.add(menuPanel);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        addStartButton(buttonPanel);
        addMenuButton(buttonPanel);
        return buttonPanel;
    }

    private void addStartButton(Container container) {
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font(null, Font.BOLD, 24));
        container.add(startButton);
        startButton.addActionListener(this);
    }

    private void addMenuButton(Container container) {
        JButton menuButton = new JButton("Menu");
        menuButton.setFont(new Font(null, Font.BOLD, 24));
        container.add(menuButton);
    }

    private void createBannerPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 3));
        statsPanel.setBackground(new Color(52, 116, 235));
        statsPanel.setVisible(true);

        // setup score label
        scoreLabel = new JLabel(" Score: " + score);
        statsPanel.add(scoreLabel, BorderLayout.WEST);

        // setup time label
        timeLabel = new JLabel("Time: " + time + " ", SwingConstants.RIGHT);
        statsPanel.add(timeLabel, BorderLayout.EAST);

        statsPanel.add(createIngameButtons(statsPanel));
        gameFrame.add(statsPanel, BorderLayout.NORTH);
    }

    private JPanel createIngameButtons(JPanel statsPanel) {
        JPanel ingameButtonPanel = new JPanel();
        ingameButtonPanel.setBackground(new Color(52, 116, 235));
        addResetButton(ingameButtonPanel);
        addExitButton(ingameButtonPanel);
        return ingameButtonPanel;
    }

    private void addResetButton(Container container) {
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font(null, Font.BOLD, 24));
        container.add(resetButton, BorderLayout.CENTER);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopGame();
                gameFrame.remove(currGamePanel);
                gameFrame.remove(statsPanel);
                createNewRound();
            }
        });
    }

    private void addExitButton(Container container) {
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font(null, Font.BOLD, 24));
        container.add(exitButton, BorderLayout.CENTER);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame(e);
            }

            private void exitGame(ActionEvent e) {
                stopGame();
                gameFrame.remove(statsPanel);
                currGamePanel.setVisible(false);
                gameFrame.remove(currGamePanel);
                currGamePanel = null;
                createMenuPanel();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menuPanel.setVisible(false);
        gameFrame.remove(menuPanel);
        createNewRound();
    }

    static void displayGameOver() {
        gameOver.setFont(new Font(null, Font.BOLD, 120));
        gameOver.setText("Game Over!");
        currGamePanel.setLayout(new GridBagLayout());
        currGamePanel.add(gameOver);
        gameOver.setVisible(true);
    }

    private void createNewRound() {
        createBannerPanel();
        currGamePanel = new StartGame(getAdjustedCurrGamePanelWidth(), (int)getAdjustedCurrGamePanelHeight());
        currGamePanel.setBorder(BorderFactory.createMatteBorder(BORDER_SIZE, 
            BORDER_SIZE, BORDER_SIZE+6, BORDER_SIZE, Color.GRAY));
        currGamePanel.setSize(getAdjustedCurrGamePanelWidth(), (int)getAdjustedCurrGamePanelHeight());
        currGamePanel.setBackground(new Color(216,223,227));
        currGamePanel.setVisible(true);
        gameFrame.getContentPane().add(currGamePanel);
        gameOver.setVisible(false);
    }

    void resetScore() {
        score = 0;
        scoreLabel.setText(" Score: " + score);
    }

    void resetTime() {
        time = 0;
        timeLabel.setText("Time: " + time + "  ");
    }

    void stopGame() {
        this.resetTime();
        this.resetScore();
        StartGame.movementTimer.stop();
        StartGame.playTimer.stop();
    }

    public static int getScore() {
        return score;
    }

    public static void increaseScore() {
        scoreLabel.setText(" Score: " + ++score);
    }

    public int getTime() {
        return time;
    }

    public static void increaseTime() {
        timeLabel.setText("Time: " + ++time + "  ");
    }

    static double getAdjustedHeight() {
        return FRAME_HEIGHT - STATS_PANEL - BORDER_SIZE - insets.top;
    }

    static int getAdjustedHeightForTopWall() {
        return BORDER_SIZE;
    }
    static int getAdjustedHeightForBotWall() {
        return FRAME_HEIGHT - BORDER_SIZE - insets.bottom - 100;
    }

    static double getAdjustedHeightForSnakeStart() {
        return (FRAME_HEIGHT - STATS_PANEL - BORDER_SIZE - insets.top - insets.bottom);
    }

    static double getAdjustedWidthForSnakeStart() {
        return FRAME_WIDTH - BORDER_SIZE - insets.left - insets.right;
    }

    static int getAdjustedWidthForLeftWall() {
        return BORDER_SIZE;
    }

    static int getAdjustedWidthForRightWall() {
        return FRAME_WIDTH - BORDER_SIZE - insets.right*2;
    }

    private int getAdjustedCurrGamePanelWidth() {
        return FRAME_WIDTH - insets.left - insets.right - BORDER_SIZE*2;
    }

    private int getAdjustedCurrGamePanelHeight() {
        return FRAME_HEIGHT - insets.bottom - insets.top - BORDER_SIZE*2;
    }
}