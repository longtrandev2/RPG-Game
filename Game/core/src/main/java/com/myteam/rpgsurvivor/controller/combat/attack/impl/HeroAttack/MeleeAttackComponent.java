package com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Entity;
import com.myteam.rpgsurvivor.model.Enemy;

import java.util.ArrayList;

public class MeleeAttackComponent {

    private final Entity owner;
    private final EnemySpawnController enemySpawnController;
    private final float attackCooldown;
    private final float attackRange;
    private final int damage;
    private final float knockbackStrength = 150f;
    private final float attackSpeed;
    private float lastAttackTime;

    public MeleeAttackComponent(Entity owner, EnemySpawnController enemySpawnController,
                                float attackSpeed, float attackRange, int damage) {
        this.owner = owner;
        this.enemySpawnController = enemySpawnController;
        this.attackCooldown = 1/ attackSpeed;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
        this.damage = damage;
        this.lastAttackTime = -attackCooldown;
    }

    public void tryAttack() {
        float currentTime = getCurrentTime();

//        if (currentTime - lastAttackTime < attackCooldown) return;
        lastAttackTime = currentTime;

        Rectangle ownerBounds = owner.getHitbox();
        float attackX = owner.isFacingRight()
            ? ownerBounds.x + ownerBounds.width
            : ownerBounds.x - attackRange;
        float attackY = ownerBounds.y;

        Rectangle attackArea = new Rectangle(attackX, attackY, attackRange, ownerBounds.height);
        owner.setAttackbox(attackArea);
        ArrayList <Enemy> enemyWave;
        if(enemySpawnController.isBossWave()) enemyWave = enemySpawnController.getActiveBoss();
        else enemyWave = enemySpawnController.getActiveEnemies();
        for (Enemy enemy : enemyWave) {
            if (enemy.getHitBox().overlaps(attackArea)) {
                System.out.println("Overlap");
                // Gây damage và knockback
//                Vector2 knockback = new Vector2(owner.isFacingRight() ? 1 : -1, 0).scl(knockbackStrength);
                enemy.takeDamge(damage);
//                enemy.applyKnockback(knockback);
                enemy.onHurt();
            }
        }

    }
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= attackSpeed;
    }

    private float getCurrentTime() {
        return System.nanoTime() / 1_000_000_000.0f;
    }
}
