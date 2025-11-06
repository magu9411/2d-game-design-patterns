package com.ooad.gameengine.state;

import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.InputEvent;

import java.awt.event.KeyEvent;

public class IdleState implements PlayerState {
    @Override
    public String name() {
        return "Idle";
    }

    @Override
    public void enter(PlayerStateComponent context, Entity entity) {
        entity.getVelocity().set(0, 0);
    }

    @Override
    public void handleInput(PlayerStateComponent context, Entity entity, InputEvent event) {
        if (isAttackKey(event)) {
            context.setState(context.attackingState());
            return;
        }
        context.noteMovementKey(event);
        if (event.getAction() == InputEvent.Action.PRESSED && context.hasMovementInput()) {
            context.setState(context.movingState());
        }
    }

    @Override
    public void update(PlayerStateComponent context, Entity entity, double deltaSeconds) {
        // nothing to do here
    }

    private boolean isAttackKey(InputEvent event) {
        return event.getKeyCode() == KeyEvent.VK_SPACE && event.getAction() == InputEvent.Action.PRESSED;
    }
}
