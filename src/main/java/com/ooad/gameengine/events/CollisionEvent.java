package com.ooad.gameengine.events;

import com.ooad.gameengine.entity.Entity;

public class CollisionEvent implements GameEvent {
    public enum CollisionType {
        ENTITY_ENTITY,  // Collision between two entities
        ENTITY_WALL,    // Collision between entity and world boundary
        NONE
    }

    private final Entity entity1;
    private final Entity entity2;  // null for wall collisions
    private final CollisionType type;
    private final long timestamp;

    public CollisionEvent(Entity entity1, Entity entity2, CollisionType type) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public EventType type() {
        return EventType.ENTITY_COLLISION;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public CollisionType getCollisionType() {
        return type;
    }

    public boolean isWallCollision() {
        return type == CollisionType.ENTITY_WALL;
    }

    public boolean isEntityCollision() {
        return type == CollisionType.ENTITY_ENTITY;
    }

    public boolean involves(Entity entity) {
        return entity1 == entity || entity2 == entity;
    }
}
