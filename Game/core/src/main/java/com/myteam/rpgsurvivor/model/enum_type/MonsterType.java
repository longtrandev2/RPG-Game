package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum MonsterType {
    GOBLIN(new EntityStat(40, 15, 20, 1.8f, 10f),EnemyHitbox.GOBLIN),
    SKELETON(new EntityStat(50, 20, 20, 1.7f, 8f), EnemyHitbox.SKELETON),
    RAT(new EntityStat(40, 30, 30, 2.2f, 12f), EnemyHitbox.RAT),
    ORC(new EntityStat(60,10,20,1.5f,10f), EnemyHitbox.ORC),
    VAMPIRE(new EntityStat(60, 10, 20,1.6f, 10f), EnemyHitbox.VAMPIRE);
//    STONE_GOLEM(new EntityStat(250, 35, 40, 1.5f, 10f));

    public final EntityStat stat;
    public final EnemyHitbox hitbox;

    MonsterType(EntityStat stat, EnemyHitbox hitbox) {

        this.stat = stat;
        this.hitbox = hitbox;
    }
}
