package com.ooad.gameengine.entity;

import com.ooad.gameengine.components.Component;
import com.ooad.gameengine.events.EventBus;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Aggregates components (Component pattern) and routes lifecycle callbacks.
 */
public class Entity {
    private static final double DEFAULT_PLAYER_HEALTH = 10.0;
    private final UUID id = UUID.randomUUID();
    private final String label;
    private final Vector2D position;
    private final Vector2D velocity;
    private final Vector2D size;
    private final List<Component> components = new ArrayList<>();
    private boolean alive = true;
    private double health = DEFAULT_PLAYER_HEALTH;
    private int worldWidth;
    private int worldHeight;
    private EventBus eventBus;

    public Entity(String label, Vector2D position, Vector2D size) {
        this.label = label;
        this.position = position;
        this.size = size;
        this.velocity = new Vector2D(0, 0);
    }

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getSize() {
        return size;
    }

    public double getHealth() {
        return health;
    }

    public void addComponent(Component component) {
        component.setOwner(this);
        components.add(component);
        component.onAdded(this);
        if (eventBus != null) {
            component.bind(eventBus);
        }
    }

    public void update(double deltaSeconds) {
        for (Component component : components) {
            component.update(this, deltaSeconds);
        }
        Vector2D delta = velocity.copy();
        delta.scale(deltaSeconds);
        position.add(delta);
    }

    public void render(Graphics2D graphics) {
        for (Component component : components) {
            component.render(this, graphics);
        }
    }

    public void attachEventBus(EventBus bus) {
        this.eventBus = bus;
        for (Component component : components) {
            component.bind(bus);
        }
    }

    public void setWorldBounds(int width, int height) {
        this.worldWidth = width;
        this.worldHeight = height;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void destroy() {
        alive = false;
        for (Component component : components) {
            component.destroy();
        }
    }
}
