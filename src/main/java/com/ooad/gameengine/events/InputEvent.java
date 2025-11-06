package com.ooad.gameengine.events;

public class InputEvent implements GameEvent {
    public enum Action {
        PRESSED,
        RELEASED
    }

    private final int keyCode;
    private final Action action;
    private final long timestamp;

    public InputEvent(int keyCode, Action action) {
        this.keyCode = keyCode;
        this.action = action;
        this.timestamp = System.nanoTime();
    }

    public int getKeyCode() {
        return keyCode;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public EventType type() {
        return EventType.INPUT;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }
}
