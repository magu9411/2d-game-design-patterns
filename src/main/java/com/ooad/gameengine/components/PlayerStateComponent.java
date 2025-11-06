package com.ooad.gameengine.components;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.EventType;
import com.ooad.gameengine.events.GameEvent;
import com.ooad.gameengine.events.InputEvent;
import com.ooad.gameengine.events.StateChangeEvent;
import com.ooad.gameengine.state.AttackingState;
import com.ooad.gameengine.state.IdleState;
import com.ooad.gameengine.state.MovingState;
import com.ooad.gameengine.state.PlayerState;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements a concrete State pattern for the player.
 */
public class PlayerStateComponent extends Component {
    private final PlayerState idleState = new IdleState();
    private final PlayerState movingState = new MovingState();
    private final PlayerState attackingState = new AttackingState();
    private PlayerState current = idleState;
    private final Set<Integer> activeMovementKeys = new HashSet<>();
    private double attackTimer;
    private boolean stateAnnounced;

    @Override
    public void onAdded(Entity entity) {
        current.enter(this, entity);
    }

    @Override
    public void update(Entity entity, double deltaSeconds) {
        if (!stateAnnounced && getOwner() != null) {
            publish(new StateChangeEvent(getOwner().getLabel(), current.name()));
            stateAnnounced = true;
        }
        current.update(this, entity, deltaSeconds);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.type() == EventType.INPUT && event instanceof InputEvent inputEvent) {
            current.handleInput(this, getOwner(), inputEvent);
        }
    }

    public void setState(PlayerState nextState) {
        if (this.current == nextState || nextState == null) {
            return;
        }
        this.current = nextState;
        current.enter(this, getOwner());
        publish(new StateChangeEvent(getOwner().getLabel(), current.name()));
        stateAnnounced = true;
    }

    public String currentStateName() {
        return current.name();
    }

    public void noteMovementKey(InputEvent event) {
        if (!isMovementKey(event.getKeyCode())) {
            return;
        }
        if (event.getAction() == InputEvent.Action.PRESSED) {
            activeMovementKeys.add(event.getKeyCode());
        } else {
            activeMovementKeys.remove(event.getKeyCode());
        }
    }

    public boolean hasMovementInput() {
        return !activeMovementKeys.isEmpty();
    }

    public PlayerState idleState() {
        return idleState;
    }

    public PlayerState movingState() {
        return movingState;
    }

    public PlayerState attackingState() {
        return attackingState;
    }

    private boolean isMovementKey(int keyCode) {
        return keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT ||
                keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN ||
                keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_A ||
                keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_D;
    }

    public void startAttackTimer(double seconds) {
        attackTimer = seconds;
    }

    public void tickAttackTimer(double deltaSeconds) {
        attackTimer -= deltaSeconds;
    }

    public boolean isAttackFinished() {
        return attackTimer <= 0;
    }
}
