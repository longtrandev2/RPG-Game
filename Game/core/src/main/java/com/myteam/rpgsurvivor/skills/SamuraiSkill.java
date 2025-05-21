package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;
import com.myteam.rpgsurvivor.input.InputHandle;

public class SamuraiSkill {
    private Samurai samurai;
    private int damage;
    private float range;

    // Dash properties
    private boolean isDashing;
    private float dashDistance;
    private float dashDuration;
    private float dashTimer;
    private Vector2 dashDirection;
    private Vector2 dashStartPosition;
    private Vector2 dashTargetPosition;
    private float dashCoolDown;
    private float lastDashTime;
    private float gameTime;

    public SamuraiSkill(Samurai samurai) {
        this.samurai = samurai;
        this.damage = 50;
        this.range = 100;
        System.out.println(samurai);
        // Initialize dash properties
        isDashing = false;
        dashDistance = 150.0f;
        dashDuration = 0.3f;
        dashTimer = 0f;
        dashDirection = new Vector2();
        dashStartPosition = new Vector2();
        dashTargetPosition = new Vector2();
        dashCoolDown = 1.5f;
        lastDashTime = -dashCoolDown; // Allow initial dash immediately
        gameTime = 0f;
    }

    public void update(float deltaTime) {
        gameTime += deltaTime;

        if (isDashing) {
            dashTimer += deltaTime;
            float progress = Math.min(dashTimer / dashDuration, 1.0f);

            // Update samurai position during dash
            float newX = dashStartPosition.x + (dashTargetPosition.x - dashStartPosition.x) * progress - 20;
            float newY = dashStartPosition.y + (dashTargetPosition.y - dashStartPosition.y) * progress - 20;
            samurai.setEntityPosition(newX, newY);

            // End dash when complete
            if (progress >= 1.0f) {
                isDashing = false;
                dashTimer = 0f;
            }
        }
    }

    public void performDash(boolean facingRight, InputHandle inputHandler) {
        // Check cooldown
        if (gameTime - lastDashTime < dashCoolDown) return;

        // Start dash
        isDashing = true;
        dashTimer = 0f;
        lastDashTime = gameTime;

        // Record starting position
        dashStartPosition.set(samurai.getEntityX(), samurai.getEntityY());

        // Determine dash direction based on input
        dashDirection.set(0, 0);

        if (inputHandler.isActionActive(InputHandle.ACTION_MOVE_UP)) {
            dashDirection.y = 1;
        }
        if (inputHandler.isActionActive(InputHandle.ACTION_MOVE_DOWN)) {
            dashDirection.y = -1;
        }
        if (inputHandler.isActionActive(InputHandle.ACTION_MOVE_LEFT)) {
            dashDirection.x = -1;
        }
        if (inputHandler.isActionActive(InputHandle.ACTION_MOVE_RIGHT)) {
            dashDirection.x = 1;
        }

        // If no direction input, dash in facing direction
        if (dashDirection.isZero()) {
            dashDirection.x = facingRight ? 1 : -1;
        } else {
            dashDirection.nor(); // Normalize for diagonal movement
        }

        // Calculate target position
        dashTargetPosition.set(
            dashStartPosition.x + dashDirection.x * dashDistance,
            dashStartPosition.y + dashDirection.y * dashDistance
        );

        // Apply screen boundary checks
        if (dashTargetPosition.x < 0) dashTargetPosition.x = 0;
        if (dashTargetPosition.y < 0) dashTargetPosition.y = 0;

        // Get screen boundaries
        float maxX = Gdx.graphics.getWidth() - samurai.getWidth();
        float maxY = Gdx.graphics.getHeight() - samurai.getHeight();

        if (dashTargetPosition.x > maxX) dashTargetPosition.x = maxX;
        if (dashTargetPosition.y > maxY) dashTargetPosition.y = maxY;
    }

    // Utility methods
    public boolean canDash() {
        return gameTime - lastDashTime >= dashCoolDown;
    }

    public boolean isDashing() {
        return isDashing;
    }

    public float getDashProgress() {
        if (!isDashing) return 0f;
        return Math.min(dashTimer / dashDuration, 1.0f);
    }

    public float getDashCoolDown() {
        return dashCoolDown;
    }

    public float getRemainingCooldown() {
        if (gameTime - lastDashTime >= dashCoolDown) return 0f;
        return dashCoolDown - (gameTime - lastDashTime);
    }

    public int getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }
}
