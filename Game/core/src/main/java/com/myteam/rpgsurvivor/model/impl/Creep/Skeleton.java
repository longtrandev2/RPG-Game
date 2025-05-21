package com.myteam.rpgsurvivor.model.impl.Creep;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class Skeleton extends Enemy {

    public Skeleton(float x, float y, Player player, AnimationForEnemy animationFactory) {
        super(x, y, MonsterType.SKELETON, player, animationFactory);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }



    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        super.render(batch, deltaTime);
        update(deltaTime);
    }



    @Override
    public boolean isDead() {
        return isDead;
    }
}
