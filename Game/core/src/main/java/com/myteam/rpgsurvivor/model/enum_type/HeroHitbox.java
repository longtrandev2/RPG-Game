package com.myteam.rpgsurvivor.model.enum_type;

import com.badlogic.gdx.math.Rectangle;

public enum HeroHitbox {
    KNIGHT(24, 34, 36, 24),
    SAMURAI(21, 33, (float) 37.5, (float)15),
    WIZARD(30, 40, 42, 24),
    ARCHER(36, 33, (float) 89.5, (float)0),
    SUMMON_KNIGHT(18, 26, 0, 0),;

    public final float width;
    public final float height;
    public final float offsetX;
    public final float offsetY;

    HeroHitbox(float width, float height, float offsetX, float offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Rectangle createHitbox(float x, float y) {
        return new Rectangle(x + offsetX, y + offsetY, width, height);
    }

    public float getOffsetX(){
        return offsetX;
    }

    public float getOffsetY(){
        return offsetY;
    }
}
