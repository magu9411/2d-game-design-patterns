package com.ooad.gameengine.components;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.CollisionEvent;
import com.ooad.gameengine.events.EventType;
import com.ooad.gameengine.events.GameEvent;
import com.ooad.gameengine.events.InputEvent;
import com.ooad.gameengine.events.StateChangeEvent;
import com.ooad.gameengine.state.*;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements a concrete State pattern for the player.
 */
public class PlayerStateComponent extends Component implements IPlayerStateProvider {
    private final PlayerState idleState = new IdleState();
    private final PlayerState movingState = new MovingState();
    private final PlayerState attackingState = new AttackingState();
    private final PlayerState damagedState = new DamagedState();
    private PlayerState current = idleState;

    private final Set<Integer> activeMovementKeys = new HashSet<>();
    private double attackTimer;
    private double damageInvulnerabilityTimer = 0;
    private static final double INVULNERABILITY_DURATION = 0.5;
    private boolean stateAnnounced;
    private boolean inContactWithAnotherEntity = false;

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
        damageInvulnerabilityTimer -= deltaSeconds;
        current.update(this, entity, deltaSeconds);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.type() == EventType.INPUT && event instanceof InputEvent inputEvent) {
            current.handleInput(this, getOwner(), inputEvent);
        } else if (event.type() == EventType.ENTITY_COLLISION && event instanceof CollisionEvent collisionEvent) {
            handleCollision(collisionEvent);
        }
    }

    private void handleCollision(CollisionEvent collision) {
        Entity owner = getOwner();
        if (!collision.involves(owner) || tookDamageRecently()) {
            return;
        }

        if (collision.isWallCollision()) {
            // Handle wall collision if needed
            return;
        }

        if (collision.isEntityCollision()) {
            // Check if the other entity is an enemy
            Entity other = collision.getEntity1() == owner ? collision.getEntity2() : collision.getEntity1();
            if (other.getLabel() != null && other.getLabel().startsWith("Enemy") && !isAttacking()) {
                setState(damagedState());
                resetInvulnerabilityTimer();
            }
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

    public PlayerState damagedState() {return damagedState;}

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

    @Override
    public boolean isAttacking() {
        return "Attacking".equals(current.name());
    }

    @Override
    public boolean tookDamageRecently() {
        return damageInvulnerabilityTimer > 0;
    }

    public void resetInvulnerabilityTimer() {
        damageInvulnerabilityTimer = INVULNERABILITY_DURATION;
    }
}
