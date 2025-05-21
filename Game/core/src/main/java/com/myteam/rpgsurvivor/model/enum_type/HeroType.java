package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum HeroType {
    KNIGHT(new EntityStat(250, 18, 90, 0.13f, 10f), HeroHitbox.KNIGHT),
    SAMURAI(new EntityStat(180, 23, 125, 0.1f, 25f), HeroHitbox.SAMURAI),
    WIZARD(new EntityStat(120, 40, 100, 0.09f, 20f), HeroHitbox.WIZARD),
    ARCHER(new EntityStat(130, 28, 140, 0.08f, 3.5f), HeroHitbox.ARCHER),
    SUMMON_KNIGHT(new EntityStat(100, 10, 150, 1.5f, 1.0f), HeroHitbox.SUMMON_KNIGHT),;
    public final EntityStat stat;
    public final HeroHitbox hitbox;
    HeroType(EntityStat stat, HeroHitbox hitbox) {
        this.stat = stat;
        this.hitbox = hitbox;
    }
}
