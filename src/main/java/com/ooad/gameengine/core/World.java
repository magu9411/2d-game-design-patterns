package com.ooad.gameengine.core;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.EventBus;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Maintains the entity list and enforces world boundaries.
 */
public class World {
    private final EventBus eventBus;
    private final List<Entity> entities = new ArrayList<>();
    private final int width;
    private final int height;

    public World(EventBus eventBus, int width, int height) {
        this.eventBus = eventBus;
        this.width = width;
        this.height = height;
    }

    public void addEntity(Entity entity) {
        entity.attachEventBus(eventBus);
        entity.setWorldBounds(width, height);
        entities.add(entity);
    }

    public void update(double deltaSeconds) {
        for (Entity entity : entities) {
            entity.update(deltaSeconds);
            keepInsideBounds(entity);
        }

        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isAlive()) {
                iterator.remove();
            }
        }
    }

    private void keepInsideBounds(Entity entity) {
        entity.getPosition().clamp(0, width - entity.getSize().getX(), 0, height - entity.getSize().getY());
    }

    public void render(Graphics2D graphics) {
        for (Entity entity : entities) {
            entity.render(graphics);
        }
    }
}
