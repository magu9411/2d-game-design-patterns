package com.ooad.gameengine.core;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/**
 * Simple wrapper around a JFrame so the rest of the engine stays testable.
 */
public class GameWindow {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final JFrame frame;
    private final GamePanel panel;

    public GameWindow() {
        frame = new JFrame("2D OO Patterns Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        panel = new GamePanel(WIDTH, HEIGHT);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
        panel.start();
        panel.requestFocusInWindow();
    }
}
