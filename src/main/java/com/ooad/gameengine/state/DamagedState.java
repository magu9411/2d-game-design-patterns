package com.ooad.gameengine.state;

import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.InputEvent;

public class DamagedState implements PlayerState {
    @Override
    public String name() {
        return "Damaged";
    }

    @Override
    public void enter(PlayerStateComponent context, Entity entity) {

    }

    @Override
    public void handleInput(PlayerStateComponent context, Entity entity, InputEvent event) {

    }

    @Override
    public void update(PlayerStateComponent context, Entity entity, double deltaSeconds) {

    }
}
