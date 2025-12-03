package com.ooad.gameengine.core;

import javax.swing.SwingUtilities;

/**
 * Entry point that boots the Swing UI on the EDT.
 */
public final class Game {
    private Game() {
        GameWindow window = new GameWindow();
        window.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
