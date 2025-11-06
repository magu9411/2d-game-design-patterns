package com.ooad.gameengine.core;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.EventBus;
import com.ooad.gameengine.events.EventType;
import com.ooad.gameengine.events.InputEvent;
import com.ooad.gameengine.events.StateChangeEvent;
import com.ooad.gameengine.factory.EntityFactory;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Swing surface that runs the game loop and wires user input to the event bus (Observer pattern).
 */
public class GamePanel extends JPanel implements KeyListener {
    private final EventBus eventBus;
    private final World world;
    private final Timer timer;
    private final EntityFactory factory;
    private long lastTickNanos;
    private String playerStateLabel = "Idle";

    public GamePanel(int width, int height) {
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        addKeyListener(this);

        this.eventBus = new EventBus();
        this.eventBus.register(event -> {
            if (event.type() == EventType.STATE_CHANGE && event instanceof StateChangeEvent stateChange) {
                if ("Player".equals(stateChange.getEntityLabel())) {
                    playerStateLabel = stateChange.getNewState();
                }
            }
        });
        this.world = new World(eventBus, width, height);
        this.factory = new EntityFactory(width, height);

        bootstrapWorld();
        this.timer = new Timer(16, e -> gameLoop());
    }

    private void bootstrapWorld() {
        List<Entity> initialEntities = factory.createDemoWorld();
        initialEntities.forEach(world::addEntity);
    }

    public void start() {
        lastTickNanos = System.nanoTime();
        timer.start();
    }

    private void gameLoop() {
        long now = System.nanoTime();
        double deltaSeconds = (now - lastTickNanos) / 1_000_000_000.0;
        lastTickNanos = now;
        world.update(deltaSeconds);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        world.render(g2d);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Player State: " + playerStateLabel, 16, 24);
        g2d.drawString("Move: Arrow/WASD, Attack: Space", 16, 42);
        g2d.dispose();
    }

    private void publishKeyEvent(int keyCode, InputEvent.Action action) {
        eventBus.publish(new InputEvent(keyCode, action));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        publishKeyEvent(e.getKeyCode(), InputEvent.Action.PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        publishKeyEvent(e.getKeyCode(), InputEvent.Action.RELEASED);
    }
}
