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
    static final int FRAME_WIDTH = 800;
    static final int FRAME_HEIGHT = 700;
    static final int STATS_PANEL = 50;
    final int BORDER_HORIZONTAL = 250;
    final int BORDER_VERTICAL = 200;
    static int score = 0;
    static int time = 0;
    private JFrame gameFrame;
    private JPanel statsPanel;
    private JPanel menuPanel;
    private JPanel currGamePanel;
    static JLabel timeLabel;
    static JLabel scoreLabel;
    JButton resetButton;
    JButton exitButton;
    TimerTask task;

    GameFrame() {
        gameFrame = new JFrame();
        currGamePanel = null;
        setGameFrameParams();
        createMenuPanel();
    }

    // set game frame parameters
    private void setGameFrameParams() {
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setTitle("Sneks!");
        gameFrame.setVisible(true);
        gameFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        gameFrame.pack();
        gameFrame.setResizable(false);
    }

    private void createMenuPanel() {
        JPanel buttonPanel = createButtonPanel();
        menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_VERTICAL, BORDER_HORIZONTAL, BORDER_VERTICAL,
                BORDER_HORIZONTAL));
        menuPanel.setLayout(new GridLayout(1, 1));
        menuPanel.setBackground(new Color(152, 16, 25));

        menuPanel.add(buttonPanel, BorderLayout.CENTER);
        gameFrame.add(menuPanel, BorderLayout.CENTER);
    }

    private void createBannerPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 3));
        // statsPanel.setPreferredSize(new Dimension(STATS_PANEL, STATS_PANEL));
        statsPanel.setBackground(new Color(52, 116, 235));

        // setup score label
        scoreLabel = new JLabel(" Score: " + score);
        statsPanel.add(scoreLabel, BorderLayout.WEST);

        // setup time label
        timeLabel = new JLabel("Time: " + time + " " , SwingConstants.RIGHT);
        statsPanel.add(timeLabel, BorderLayout.EAST);

        statsPanel.add(createIngameButtons(statsPanel));
        gameFrame.add(statsPanel, BorderLayout.NORTH);
        statsPanel.setVisible(true);
    }

    private JPanel createIngameButtons(JPanel statsPanel) {
        JPanel ingameButtonPanel = new JPanel();
        addResetButton(ingameButtonPanel);
        addExitButton(ingameButtonPanel);
        return ingameButtonPanel;
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
                createBannerPanel();
                currGamePanel = new StartGame(gameFrame.getWidth() - 35, gameFrame.getHeight() - 110);
                gameFrame.add(currGamePanel);
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
                Component c = (Component) e.getSource();
                statsPanel.remove(scoreLabel);
                statsPanel.remove(timeLabel);
                statsPanel.remove(resetButton);
                statsPanel.remove(exitButton);
                statsPanel.setVisible(false);
                gameFrame.remove(statsPanel);
                c.getParent().setVisible(false);
                c.getParent().remove(currGamePanel);
                currGamePanel.setVisible(false);;
                currGamePanel = null;
                createMenuPanel();
                menuPanel.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menuPanel.setVisible(false);
        gameFrame.remove(menuPanel);
        createBannerPanel();
        gameFrame.add(currGamePanel = new StartGame(gameFrame.getWidth() - 35, gameFrame.getHeight() - 110));
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
        System.out.println("score: " + score);
        scoreLabel.setText(" Score: " + ++score);
    }

    public int getTime() {
        return time;
    }

    public static void increaseTime() {
        timeLabel.setText("Time: " + ++time + "  ");
    }
}