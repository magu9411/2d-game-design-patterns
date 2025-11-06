package com.ooad.gameengine.events;

public interface GameEvent {
    EventType type();

    long timestamp();
}
