package com.ooad.gameengine.core;

import com.ooad.gameengine.components.IPlayerStateProvider;
import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.CollisionEvent;
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

        checkCollisions();

        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isAlive()) {
                iterator.remove();
            }
        }
    }

    private void checkCollisions() {
        // First, check for wall (world boundary) collisions
        for (Entity entity : entities) {
            checkWallCollision(entity);
        }

        // Check for entity-entity collisions (including walls)
        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity b = entities.get(j);
                if (a.collidesWith(b)) {
                    // Handle wall-entity collisions
                    if (a.getLabel() != null && a.getLabel().startsWith("Wall")) {
                        resolveWallCollision(b, a);
                    } else if (b.getLabel() != null && b.getLabel().startsWith("Wall")) {
                        resolveWallCollision(a, b);
                    } else {
                        // Regular entity-entity collision
                        CollisionEvent collision = new CollisionEvent(a, b, CollisionEvent.CollisionType.ENTITY_ENTITY);
                        eventBus.publish(collision);
                    }
                }
            }
        }
    }

/**
     * Resolves collision between an entity and a wall
     * @param entity The moving entity that collided with the wall
     * @param wall The wall entity
     */
    private void resolveWallCollision(Entity entity, Entity wall) {
        // Calculate the center points
        double entityCenterX = entity.getPosition().getX() + entity.getSize().getX() / 2;
        double entityCenterY = entity.getPosition().getY() + entity.getSize().getY() / 2;
        double wallCenterX = wall.getPosition().getX() + wall.getSize().getX() / 2;
        double wallCenterY = wall.getPosition().getY() + wall.getSize().getY() / 2;

        // Calculate the overlap on each axis
        double overlapX = (entity.getSize().getX() / 2 + wall.getSize().getX() / 2) - Math.abs(entityCenterX - wallCenterX);
        double overlapY = (entity.getSize().getY() / 2 + wall.getSize().getY() / 2) - Math.abs(entityCenterY - wallCenterY);

        // Resolve collision along the axis of least penetration
        if (overlapX < overlapY) {
            // Resolve on X axis
            if (entityCenterX < wallCenterX) {
                entity.getPosition().setX(wall.getPosition().getX() - entity.getSize().getX());
            } else {
                entity.getPosition().setX(wall.getPosition().getX() + wall.getSize().getX());
            }
            // Reverse X velocity for bounce effect
            entity.getVelocity().setX(-entity.getVelocity().getX() * 0.5);
        } else {
            // Resolve on Y axis
            if (entityCenterY < wallCenterY) {
                entity.getPosition().setY(wall.getPosition().getY() - entity.getSize().getY());
            } else {
                entity.getPosition().setY(wall.getPosition().getY() + wall.getSize().getY());
            }
            // Reverse Y velocity for bounce effect
            entity.getVelocity().setY(-entity.getVelocity().getY() * 0.5);
        }
    }

    private void checkWallCollision(Entity entity) {
        double x = entity.getPosition().getX();
        double y = entity.getPosition().getY();
        double width = entity.getSize().getX();
        double height = entity.getSize().getY();
        boolean collided = false;
        double pushBack = 1.0; // Amount to push back from the wall
        double bounceFactor = 0.5; // Velocity reflection factor (0.0 = no bounce, 1.0 = full bounce)

        // Check left wall collision
        if (x < 0) {
            entity.getPosition().setX(0);
            entity.getVelocity().setX(Math.abs(entity.getVelocity().getX()) * bounceFactor);
            collided = true;
        } 
        // Check right wall collision
        else if (x + width > this.width) {
            entity.getPosition().setX(this.width - width);
            entity.getVelocity().setX(-Math.abs(entity.getVelocity().getX()) * bounceFactor);
            collided = true;
        }

        // Check top wall collision
        if (y < 0) {
            entity.getPosition().setY(0);
            entity.getVelocity().setY(Math.abs(entity.getVelocity().getY()) * bounceFactor);
            collided = true;
        } 
        // Check bottom wall collision
        else if (y + height > this.height) {
            entity.getPosition().setY(this.height - height);
            entity.getVelocity().setY(-Math.abs(entity.getVelocity().getY()) * bounceFactor); // Bounce off the wall
            collided = true;
        }

        if (collided) {
            // Publish wall collision event
            CollisionEvent collision = new CollisionEvent(entity, null, CollisionEvent.CollisionType.ENTITY_WALL);
            eventBus.publish(collision);
            
            // Apply a small push away from the wall to prevent sticking
            double pushX = 0;
            double pushY = 0;
            
            if (x < pushBack) pushX = pushBack;
            if (y < pushBack) pushY = pushBack;
            if (x + width > this.width - pushBack) pushX = -pushBack;
            if (y + height > this.height - pushBack) pushY = -pushBack;
            
            if (pushX != 0 || pushY != 0) {
                entity.getPosition().add(pushX, pushY);
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
