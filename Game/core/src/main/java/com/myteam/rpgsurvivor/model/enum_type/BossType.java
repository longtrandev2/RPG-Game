package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum BossType {
    SLIME_BOSS(new EntityStat(100,100,10,2.0f,20f), EnemyHitbox.SLIME_BOSS);


    public final EntityStat stat;
    public final EnemyHitbox hitbox;

    BossType(EntityStat stat, EnemyHitbox hitbox) {

        this.stat = stat;
        this.hitbox = hitbox;
    }
}
