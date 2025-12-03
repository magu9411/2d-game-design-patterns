package com.ooad.gameengine.components;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.events.EventType;
import com.ooad.gameengine.events.GameEvent;
import com.ooad.gameengine.events.InputEvent;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Observes keyboard events and translates them into entity velocity changes.
 */
public class PlayerInputComponent extends Component {
    private final double moveSpeed;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public PlayerInputComponent(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.type() != EventType.INPUT || !(event instanceof InputEvent input)) {
            return;
        }

        if (input.getAction() == InputEvent.Action.PRESSED) {
            pressedKeys.add(input.getKeyCode());
        } else {
            pressedKeys.remove(input.getKeyCode());
        }
    }

    @Override
    public void update(Entity entity, double deltaSeconds) {
        double vx = 0;
        double vy = 0;

        if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) {
            vx -= moveSpeed;
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) {
            vx += moveSpeed;
        }
        if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_W)) {
            vy -= moveSpeed;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) {
            vy += moveSpeed;
        }

        entity.getVelocity().set(vx, vy);
    }
}
