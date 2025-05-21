package com.myteam.rpgsurvivor.model.impl.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Enemy;

import com.myteam.rpgsurvivor.debug.DebugRenderer;
import java.util.List;

public class Arrow {
    private final Vector2 position;
    private final float speed = 200f;
    private final boolean facingRight;
    private final AnimationManager animationManager;
    private boolean isDestroyed = false;
    private final Rectangle hitbox;

    public Arrow(float x, float y, boolean facingRight, AnimationManager arrowAnimManager) {
        this.position = new Vector2(x, y);
        this.facingRight = facingRight;
        this.animationManager = arrowAnimManager.copy();
        this.animationManager.setState("arrow", true);
        this.hitbox = new Rectangle(x + 15, y + 16.5f, 20, 4);
    }

    public void update(float deltaTime, List<Enemy> enemyList, int arrowDamage) {
        if (isDestroyed) return;
        System.out.println("updating arrow");
        float dx = speed * deltaTime * (facingRight ? 1 : -1);
        position.x += dx;
        hitbox.setPosition(position.x, position.y + 16.5f);
        animationManager.update(deltaTime);
        DebugRenderer.drawRect(hitbox, Color.ORANGE);
        if (position.x < 0 || position.x > Gdx.graphics.getWidth() || position.y < 0 || position.y > Gdx.graphics.getHeight()) {
            isDestroyed = true;
            return;
        }

        for (Enemy enemy : enemyList) {
            if (hitbox.overlaps(enemy.getHitbox())) {
                enemy.takeDamge(arrowDamage);
                enemy.onHurt();
                isDestroyed = true;
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (isDestroyed) return;

        TextureRegion frame = animationManager.getCurrentFrame();
        if (frame != null) {
            System.out.println("not null");
            float width = frame.getRegionWidth();
            float height = frame.getRegionHeight();
            System.out.println("width: " + width + " height: " + height);
            float drawX = facingRight ? position.x : position.x + width;
            float scaleX = facingRight ? 1f : -1f;

            batch.draw(frame, drawX + 15f, position.y + 16.5f, width * scaleX, height);
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
