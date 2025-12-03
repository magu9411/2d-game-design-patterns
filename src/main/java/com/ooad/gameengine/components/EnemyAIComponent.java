package com.ooad.gameengine.components;

import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.entity.Vector2D;

/**
 * Bare-bones AI that patrols across a range and gently bobs vertically.
 */
public class EnemyAIComponent extends Component {
    private final double minX;
    private final double maxX;
    private final double speed;
    private double timer;
    private int direction = 1;

    public EnemyAIComponent(double minX, double maxX, double speed) {
        this.minX = minX;
        this.maxX = maxX;
        this.speed = speed;
    }

    @Override
    public void update(Entity entity, double deltaSeconds) {
        Vector2D position = entity.getPosition();
        Vector2D velocity = entity.getVelocity();
        double nextX = position.getX() + direction * speed * deltaSeconds;
        if (nextX < minX) {
            direction = 1;
        } else if (nextX + entity.getSize().getX() > maxX) {
            direction = -1;
        }
        velocity.set(direction * speed, Math.sin(timer * 2) * 10);
        timer += deltaSeconds;
    }
}
