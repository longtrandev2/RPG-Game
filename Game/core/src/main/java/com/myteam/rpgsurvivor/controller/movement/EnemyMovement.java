package com.myteam.rpgsurvivor.controller.movement;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.model.Player;

public class EnemyMovement extends Movement {
    private float enemyX;
    private float enemyY;
    private Player player;
    private Rectangle hitboxPlayer;
    private float moveSpeed;
    private float minDistanceToPlayer;
    private boolean isMoving;

    public EnemyMovement(float enemyX, float enemyY, Player player, float moveSpeed) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.player = player;
        this.moveSpeed = moveSpeed;
        this.minDistanceToPlayer = 10f;
        this.isMoving = false;
        this.hitboxPlayer = player.getHitbox();
    }

    public Vector2 move(float deltaTime) {
        float playerX = hitboxPlayer.getX();
        float playerY = hitboxPlayer.getY();

        Vector2 direction = new Vector2(playerX - enemyX, playerY - enemyY);
        Vector2 newPosition = new Vector2(enemyX, enemyY);

        float distance = direction.len();
        isMoving = false;

        if (distance > minDistanceToPlayer) {
            direction.nor();

            float newX = enemyX + direction.x * moveSpeed * deltaTime;
            float newY = enemyY + direction.y * moveSpeed * deltaTime;

            // Cập nhật vị trí được theo dõi trong controller
            this.enemyX = newX;
            this.enemyY = newY;

            // Trả về vị trí mới cho entity
            newPosition.set(newX, newY);
            isMoving = true;
        }

        return newPosition;
    }

    public void updateEnemyPosition(float x, float y) {
        this.enemyX = x;
        this.enemyY = y;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setMinDistanceToPlayer(float minDistance) {
        this.minDistanceToPlayer = minDistance;
    }

    public float getDistanceToPlayer() {
        return Vector2.dst(enemyX, enemyY, player.getEntityX(), player.getEntityY());
    }
}
