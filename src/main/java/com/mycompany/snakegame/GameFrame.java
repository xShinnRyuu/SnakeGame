package com.mycompany.snakegame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

class GameFrame extends JFrame implements ActionListener {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static final int FRAME_WIDTH = 850;
    static final int FRAME_HEIGHT = 900;
    static final int STATS_PANEL = 50;
    final int BORDER_HORIZONTAL = 245;
    final int BORDER_VERTICAL = 200;
    final int VERTICAL_ALIGNMENT = 110;
    final int HORIZONTAL_ALIGNMENT = 20;
    static int score = 0;
    static int time = 0;
    static final int BORDER_SIZE = 5;
    private JFrame gameFrame;
    private JPanel statsPanel;
    private JPanel menuPanel;
    private JPanel currGamePanel;
    static JLabel timeLabel;
    static JLabel scoreLabel;
    JButton resetButton;
    JButton exitButton;
    TimerTask task;

    // TODO: ADD a border
    GameFrame() {
        gameFrame = new JFrame();
        setGameFrameParams();
        createMenuPanel();
    }

    // set game frame parameters
    private void setGameFrameParams() {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        gameFrame.setLocation(size.width/2 - FRAME_HEIGHT/2, size.height/2 - FRAME_WIDTH/2);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setTitle("Sneks!");
        // gameFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        gameFrame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        gameFrame.setVisible(true);
        // gameFrame.setResizable(false);
        // gameFrame.pack();
        // return gameFrame;
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

    private void createBannerPanel() {
        statsPanel = new JPanel();
        statsPanel.setSize(new Dimension(FRAME_WIDTH, STATS_PANEL));
        statsPanel.setLayout(new GridLayout(1, 3));
        statsPanel.setBackground(new Color(52, 116, 235));
        statsPanel.setVisible(true);

        // setup score label
        scoreLabel = new JLabel(" Score: " + score);
        statsPanel.add(scoreLabel, BorderLayout.WEST);

        // setup time label
        timeLabel = new JLabel("Time: " + time + " " , SwingConstants.RIGHT);
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

    private void addStartButton(Container container) {
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font(null, Font.BOLD, 24));
        container.add(startButton);
        startButton.addActionListener(this);
    }

    private void addMenuButton(Container container) {
        JButton menuButton = new JButton("Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setFont(new Font(null, Font.BOLD, 24));
        container.add(menuButton);
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

    private void createNewRound() {
        createBannerPanel();
        currGamePanel = new StartGame(FRAME_WIDTH, FRAME_HEIGHT - STATS_PANEL);
        currGamePanel.setBorder(BorderFactory.createMatteBorder(BORDER_SIZE, 
            BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, Color.GRAY));
        currGamePanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - STATS_PANEL));
        currGamePanel.setBackground(new Color(216,223,227));
        gameFrame.getContentPane().add(currGamePanel);
        gameFrame.pack();
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

    public static double getAdjustedHeight() {
        return FRAME_HEIGHT - STATS_PANEL + BORDER_SIZE;
    }

    public static double getAdjustedWidth() {
        return FRAME_WIDTH + BORDER_SIZE;
    }
}