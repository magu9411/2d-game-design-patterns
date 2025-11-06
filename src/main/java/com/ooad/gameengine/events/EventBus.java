package com.ooad.gameengine.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe event dispatcher (Observer pattern).
 */
public class EventBus {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void register(EventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    public void publish(GameEvent event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
