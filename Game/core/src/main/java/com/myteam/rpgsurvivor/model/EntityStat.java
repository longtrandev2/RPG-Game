package com.myteam.rpgsurvivor.model;

public class EntityStat {
    public int maxHealth;
    public int damage;
    public float moveSpeed;
    public float attackSpeed;
    public float rangeAttack;

    public EntityStat(int maxHealth, int damage, float moveSpeed, float attackSpeed, float rangeAttack) {
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.moveSpeed = moveSpeed;
        this.attackSpeed = attackSpeed;
        this.rangeAttack = rangeAttack;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
}
