package com.ooad.gameengine.components;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.EventBus;
import com.ooad.gameengine.events.EventListener;
import com.ooad.gameengine.events.GameEvent;

import java.awt.Graphics2D;

/**
 * Base class for all components that supports event subscription.
 */
public abstract class Component implements EventListener {
    private EventBus eventBus;
    private Entity owner;

    public void bind(EventBus eventBus) {
        if (this.eventBus == eventBus) {
            return;
        }
        if (this.eventBus != null) {
            this.eventBus.unregister(this);
        }
        this.eventBus = eventBus;
        if (eventBus != null) {
            eventBus.register(this);
        }
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public Entity getOwner() {
        return owner;
    }

    protected EventBus getEventBus() {
        return eventBus;
    }

    protected void publish(GameEvent event) {
        if (eventBus != null) {
            eventBus.publish(event);
        }
    }

    public void onAdded(Entity entity) {
        // optional hook
    }

    public void update(Entity entity, double deltaSeconds) {
        // default no-op
    }

    public void render(Entity entity, Graphics2D graphics) {
        // default no-op
    }

    public void destroy() {
        if (eventBus != null) {
            eventBus.unregister(this);
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        // default no-op
    }
}
