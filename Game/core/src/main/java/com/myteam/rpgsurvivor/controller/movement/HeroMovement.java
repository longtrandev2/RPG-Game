package com.myteam.rpgsurvivor.controller.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.model.Entity;

public class HeroMovement extends Movement{
    private Entity entity;
    private Vector2 direction;
    private boolean isMoving;

    // Movement directions
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    // Map boundaries
    private float mapWidth;
    private float mapHeight;
    private float mapX;
    private float mapY;

    public HeroMovement(Entity entity) {
        this.entity = entity;
        System.out.println(entity);
        this.direction = new Vector2(0, 0);
        this.isMoving = false;

        // Default map boundaries to screen size
        this.mapX = 0;
        this.mapY = 0;
        this.mapWidth = Gdx.graphics.getWidth();
        this.mapHeight = Gdx.graphics.getHeight();
    }

    /**
     * Set map boundaries
     * @param x Left boundary of the map
     * @param y Bottom boundary of the map
     * @param width Map width
     * @param height Map height
     */
    public void setMapBoundaries(float x, float y, float width, float height) {
        this.mapX = x;
        this.mapY = y;
        this.mapWidth = width;
        this.mapHeight = height;
    }

    /**
     * Set movement direction based on input
     * @param up Move up
     * @param down Move down
     * @param left Move left
     * @param right Move right
     */
    public void setMovementDirection(boolean up, boolean down, boolean left, boolean right) {
        this.moveUp = up;
        this.moveDown = down;
        this.moveLeft = left;
        this.moveRight = right;

        // Calculate direction vector
        calculateDirection();
    }

    /**
     * Calculate movement direction vector based on pressed keys
     */
    private void calculateDirection() {
        direction.set(0, 0);

        if (moveUp) direction.y += 1;
        if (moveDown) direction.y -= 1;
        if (moveLeft) direction.x -= 1;
        if (moveRight) direction.x += 1;

        // Normalize for diagonal movement
        if (direction.len2() > 0) {
            direction.nor();
            isMoving = true;
        } else {
            isMoving = false;
        }
    }

    /**
     * Update entity position based on movement direction and speed
     */
    public void update() {
        if (isMoving) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            float speed = entity.getMoveSpeed();

            float newX = entity.getEntityX() + direction.x * speed * deltaTime;
            float newY = entity.getEntityY() + direction.y * speed * deltaTime;

            float entityWidth = entity.getWidth();
            float entityHeight = entity.getHeight();


            if (newX < mapX) {
                newX = mapX;
            }

            if (newX > mapX + mapWidth - entityWidth) {
                newX = mapX + mapWidth - entityWidth;
            }

            if (newY < mapY) {
                newY = mapY;
            }

            if (newY > mapY + mapHeight - entityHeight) {
                newY = mapY + mapHeight - entityHeight;
            }


            entity.setEntityPosition(newX, newY);

            updateFacingDirection();
        }
    }

    /**
     * Update entity facing direction based on movement
     */
    private void updateFacingDirection() {
        if (direction.x < 0) {
            entity.setFacingRight(false);
        } else if (direction.x > 0) {
            entity.setFacingRight(true);
        }
    }

    /**
     * Check if entity is moving
     * @return true if entity is moving
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Get current movement direction
     * @return Vector2 representing movement direction
     */
    public Vector2 getDirection() {
        return direction;
    }

    /**
     * Check which direction the entity is moving
     * @return Direction code: 0=idle, 1=up, 2=right, 3=down, 4=left, 5-8=diagonal directions
     */
    public int getDirectionCode() {
        if (!isMoving) return 0;

        if (direction.x > 0.5f && Math.abs(direction.y) < 0.5f) return 2; // right
        if (direction.x < -0.5f && Math.abs(direction.y) < 0.5f) return 4; // left
        if (Math.abs(direction.x) < 0.5f && direction.y > 0.5f) return 1; // up
        if (Math.abs(direction.x) < 0.5f && direction.y < -0.5f) return 3; // down


        if (direction.x > 0 && direction.y > 0) return 5; // up-right
        if (direction.x > 0 && direction.y < 0) return 6; // down-right
        if (direction.x < 0 && direction.y < 0) return 7; // down-left
        if (direction.x < 0 && direction.y > 0) return 8; // up-left

        return 0;
    }
}
