package com.myteam.rpgsurvivor.model.impl.Creep;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;

public class Orc extends Enemy {
    public Orc(float x, float y, Player player, AnimationForEnemy animationFactory) {
        super(x, y, MonsterType.ORC, player, animationFactory);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        super.render(batch, deltaTime);
    }
}
