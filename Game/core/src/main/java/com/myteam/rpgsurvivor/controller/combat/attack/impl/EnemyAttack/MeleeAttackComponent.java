package com.myteam.rpgsurvivor.controller.combat.attack.impl.EnemyAttack;

import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.AttackComponent;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class MeleeAttackComponent implements AttackComponent {
    private final Enemy attacker;
    private final Player targetPlayer;
    private final float attackCooldown;
    private float attackTimer;
    private final Rectangle attackBox;
    private final float offsetX, offsetY;

    public MeleeAttackComponent(Enemy attacker, Player targetPlayer, float attackRange, float attackSpeed, float offsetX, float offsetY) {
        this.attacker = attacker;
        this.targetPlayer = targetPlayer;
        this.attackCooldown = 1f / attackSpeed;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.attackBox = new Rectangle(attacker.getHitbox());
        this.attackBox.setSize(attacker.getHitbox().getWidth() + attackRange, attacker.getHitbox().getHeight() + attackRange);
    }

    @Override
    public void update(float deltaTime) {
        if (attacker.isFacingRight()) {
            attackBox.setPosition(attacker.getEntityX() + offsetX, attacker.getEntityY() + offsetY);
        } else {
            attackBox.setPosition(attacker.getEntityX() + offsetX - attackBox.getWidth(), attacker.getEntityY() + offsetY);
        }

        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }
        if (attackBox.overlaps(targetPlayer.getHitbox()) && attackTimer <= 0 && !attacker.isAttack()) {
            attacker.setState(StateType.STATE_ATTACK.stateType);
            attackTimer = attackCooldown;
            targetPlayer.takeDamge(attacker.getDamage());
            targetPlayer.onHurt();
        }
    }

    @Override
    public void performAttack() {
        if (attackTimer <= 0 && !attacker.isAttack()) {
            attackTimer = attackCooldown;
            attacker.setAttack();
            attacker.setState(StateType.STATE_ATTACK.stateType);
            targetPlayer.takeDamge(attacker.getDamage());
            targetPlayer.onHurt();
        }
    }
    public Rectangle getAttackBox() {
        return attackBox;
    }

    public Rectangle getTargetBox() {
        return targetPlayer.getHitbox();
    }
}
