package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.MeleeAttackComponent;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class Player extends Entity{

    protected int level;
    protected int experience;
    protected int mana;
    protected int skillPoints;
    protected int healthPoints;
    protected int damagePoints;
    protected int speedPoints;
    protected int atkSpeedPoints;
    protected EnemySpawnController enemySpawnController;
    protected HeroType heroType;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;

    private ShapeRenderer shapeRenderer;
    private float offsetX;
    private float offsetY;

    protected  MeleeAttackComponent attackHandler;
    protected boolean attackTriggered = false;

    public Player(float x, float y, HeroType heroType) {
        this.entityX = x;
        this.entityY = y;

        this.heroType = heroType;

        this.stat = new EntityStat(
            heroType.stat.maxHealth,
            heroType.stat.damage,
            heroType.stat.moveSpeed,
            heroType.stat.attackSpeed,
            heroType.stat.rangeAttack
        );

        this.level = 1;
        this.experience = 0;
        this.mana = 100;
        this.skillPoints = 10;
        this.healthPoints = 0;
        this.damagePoints = 0;
        this.speedPoints = 0;
        this.atkSpeedPoints = 0;
        this.currentHealth = stat.maxHealth;
        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;

        this.hitbox = heroType.hitbox.createHitbox(entityX , entityY);
        this.attackbox = new Rectangle(hitbox);
        offsetX = heroType.hitbox.getOffsetX();
        offsetY = heroType.hitbox.getOffsetY();
    }

    @Override
    public void update(float deltaTime){
        this.setCurrentHealth(max(0, this.currentHealth));
        hitbox.setPosition(entityX + offsetX  ,entityY + offsetY);
        DebugRenderer.drawRect(attackbox, Color.RED);
    }
    public abstract void onHurt();

    public void setEnemySpawnController (EnemySpawnController controller)
    {

        this.enemySpawnController = controller;
        setMeleeAttackComponent();
    }

    public ArrayList<Enemy> getEnemies()
    {
        return enemySpawnController.getActiveEnemies();
    }

    public void performAttack() {

    }
    public void setMeleeAttackComponent(){
        this.attackHandler = new MeleeAttackComponent(this, enemySpawnController, this.getAttackSpeed(), this.getRangeAttack(), this.getDamage());
    }

    public void increaseHealth()
    {
        this.setMaxHealth(getMaxHealth() + 10);
    }

    public void decreaseHealth()
    {
        this.setMaxHealth(getMaxHealth() - 10);
    }

    public void increaseDamage()
    {
        this.setDamage(getDamage() + 5);
    }

    public void decreaseDamage()
    {
        this.setDamage(getDamage() - 5);
    }

    public void increaseMoveSpeed()
    {
        this.setMoveSpeed(getMoveSpeed() + 2f);
    }

    public void decreaseMoveSpeed()
    {
        this.setMoveSpeed(getMoveSpeed() - 2f);
    }

    public void increaseAttackSpeed()
    {
        this.setAttackSpeed(getAttackSpeed() + 0.2f);
    }

    public void decreaseAttackSpeed()
    {
        this.setAttackSpeed(getAttackSpeed() - 0.2f);
    }


    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints)
    {
        this.skillPoints = skillPoints;
    }

    public void addSkillPoints(int points) {
        this.skillPoints += points;
    }

    public void deSpendSkillPointOnHealth() {
            System.out.println(healthPoints);
            skillPoints++;
            healthPoints--;
            decreaseHealth();

    }

    public void deSpendSkillPointOnDamage() {
            System.out.println(damagePoints);
            skillPoints++;
            damagePoints--;
            decreaseDamage();
    }

    public void deSpendSkillPointOnSpeed() {
            System.out.println(speedPoints);
            skillPoints++;
            speedPoints--;
            decreaseMoveSpeed();
    }

    public void deSpendSkillPointOnAttackSpeed() {
            System.out.println(atkSpeedPoints);
            skillPoints++;
            atkSpeedPoints--;
            decreaseAttackSpeed();
    }

    public void spendSkillPointOnHealth() {
            skillPoints--;
            healthPoints++;
            increaseHealth();
    }

    public void spendSkillPointOnDamage() {
            skillPoints--;
            damagePoints++;
            increaseDamage();

    }

    public void spendSkillPointOnSpeed() {
            skillPoints--;
            speedPoints++;
            increaseMoveSpeed();
    }

    public void spendSkillPointOnAttackSpeed() {
            skillPoints--;
            atkSpeedPoints++;
            increaseAttackSpeed();

    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public int getSpeedPoints() {
        return speedPoints;
    }

    public int getAttackSpeedPoints() {
        return atkSpeedPoints;
    }

}
