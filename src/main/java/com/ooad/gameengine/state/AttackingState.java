package com.ooad.gameengine.state;

import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.InputEvent;

public class AttackingState implements PlayerState {
    private static final double ATTACK_DURATION = 0.35;

    @Override
    public String name() {
        return "Attacking";
    }

    @Override
    public void enter(PlayerStateComponent context, Entity entity) {
        context.startAttackTimer(ATTACK_DURATION);
        entity.getVelocity().set(0, 0);
    }

    @Override
    public void handleInput(PlayerStateComponent context, Entity entity, InputEvent event) {
        // allow buffering of the next movement
        context.noteMovementKey(event);
    }

    @Override
    public void update(PlayerStateComponent context, Entity entity, double deltaSeconds) {
        context.tickAttackTimer(deltaSeconds);
        if (context.isAttackFinished()) {
            if (context.hasMovementInput()) {
                context.setState(context.movingState());
            } else {
                context.setState(context.idleState());
            }
        }
    }
}
