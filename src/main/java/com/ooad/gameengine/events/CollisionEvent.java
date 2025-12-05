package com.ooad.gameengine.events;

import com.ooad.gameengine.entity.Entity;

public class CollisionEvent implements GameEvent {
    @Override
    public EventType type() {
        return null;
    }

    @Override
    public long timestamp() {
        return 0;
    }

    public enum CollisionType {
        NONE,
        ENEMY,
        WALL
    }

    private CollisionType collisionType;
    CollisionEvent(Entity entity, CollisionType collisionType) {
        this.collisionType = collisionType;
    }
}
