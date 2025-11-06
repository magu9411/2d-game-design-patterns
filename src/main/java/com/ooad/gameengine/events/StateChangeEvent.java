package com.ooad.gameengine.events;

public class StateChangeEvent implements GameEvent {
    private final String entityLabel;
    private final String newState;
    private final long timestamp = System.nanoTime();

    public StateChangeEvent(String entityLabel, String newState) {
        this.entityLabel = entityLabel;
        this.newState = newState;
    }

    public String getEntityLabel() {
        return entityLabel;
    }

    public String getNewState() {
        return newState;
    }

    @Override
    public EventType type() {
        return EventType.STATE_CHANGE;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }
}
