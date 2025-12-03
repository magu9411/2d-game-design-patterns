package com.ooad.gameengine.factory;

import com.ooad.gameengine.components.EnemyAIComponent;
import com.ooad.gameengine.components.PlayerInputComponent;
import com.ooad.gameengine.components.PlayerStateComponent;
import com.ooad.gameengine.components.RectangleRenderComponent;
import com.ooad.gameengine.entity.Entity;
import com.ooad.gameengine.entity.Vector2D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the Factory pattern for creating game entities with the right mix of components.
 */
public class EntityFactory {
    private final int worldWidth;
    private final int worldHeight;

    public EntityFactory(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public List<Entity> createDemoWorld() {
        List<Entity> entities = new ArrayList<>();
        entities.add(createPlayer());
        entities.add(createEnemy("Enemy-A", worldWidth * 0.2, worldHeight * 0.3));
        entities.add(createEnemy("Enemy-B", worldWidth * 0.65, worldHeight * 0.55));
        return entities;
    }

    public Entity createPlayer() {
        Entity player = new Entity("Player",
                new Vector2D(worldWidth / 2.0 - 24, worldHeight - 140),
                new Vector2D(48, 48));
        player.addComponent(new PlayerInputComponent(220));
        player.addComponent(new PlayerStateComponent());
        player.addComponent(new RectangleRenderComponent(new Color(0x66F7AF), new Color(0x082032)));
        return player;
    }

    public Entity createEnemy(String label, double x, double y) {
        Entity enemy = new Entity(label, new Vector2D(x, y), new Vector2D(44, 44));
        enemy.addComponent(new EnemyAIComponent(32, worldWidth - 32, 90));
        enemy.addComponent(new RectangleRenderComponent(new Color(0xFF6B6B), new Color(0x330000)));
        return enemy;
    }
}
