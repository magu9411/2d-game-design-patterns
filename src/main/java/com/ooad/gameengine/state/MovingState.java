package com.ooad.gameengine.state;

import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.InputEvent;

import java.awt.event.KeyEvent;

public class MovingState implements PlayerState {
    @Override
    public String name() {
        return "Moving";
    }

    @Override
    public void enter(PlayerStateComponent context, Entity entity) {
        // velocity comes from the input component, so nothing else needed
    }

    @Override
    public void handleInput(PlayerStateComponent context, Entity entity, InputEvent event) {
        if (isAttackKey(event)) {
            context.setState(context.attackingState());
            return;
        }

        context.noteMovementKey(event);
        if (!context.hasMovementInput()) {
            context.setState(context.idleState());
        }
    }

    @Override
    public void update(PlayerStateComponent context, Entity entity, double deltaSeconds) {
        if (!context.hasMovementInput()) {
            context.setState(context.idleState());
        }
    }

    private boolean isAttackKey(InputEvent event) {
        return event.getKeyCode() == KeyEvent.VK_SPACE && event.getAction() == InputEvent.Action.PRESSED;
    }
}
