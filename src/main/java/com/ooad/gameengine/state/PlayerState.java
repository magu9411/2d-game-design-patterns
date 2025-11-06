package com.ooad.gameengine.state;

import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.InputEvent;

public interface PlayerState {
    String name();

    void enter(PlayerStateComponent context, Entity entity);

    void handleInput(PlayerStateComponent context, Entity entity, InputEvent event);

    void update(PlayerStateComponent context, Entity entity, double deltaSeconds);
}
