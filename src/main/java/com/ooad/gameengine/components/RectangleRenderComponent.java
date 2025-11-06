package com.ooad.gameengine.components;

import com.ooad.gameengine.entity.Entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Simple renderer that draws each entity as a rounded rectangle with an outline.
 */
public class RectangleRenderComponent extends Component {
    private final Color fill;
    private final Color outline;

    public RectangleRenderComponent(Color fill, Color outline) {
        this.fill = fill;
        this.outline = outline;
    }

    @Override
    public void render(Entity entity, Graphics2D graphics) {
        graphics.setColor(fill);
        int x = (int) entity.getPosition().getX();
        int y = (int) entity.getPosition().getY();
        int w = (int) entity.getSize().getX();
        int h = (int) entity.getSize().getY();
        graphics.fillRoundRect(x, y, w, h, 16, 16);
        graphics.setColor(outline);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRoundRect(x, y, w, h, 16, 16);
    }
}
