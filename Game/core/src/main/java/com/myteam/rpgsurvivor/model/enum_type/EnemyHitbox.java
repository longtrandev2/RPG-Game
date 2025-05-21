package com.myteam.rpgsurvivor.model.enum_type;

import com.badlogic.gdx.math.Rectangle;

public enum EnemyHitbox {
    GOBLIN(17,16,0,-2),
    RAT(19,12,6,0),
    SKELETON(22,32,5,3),
    ORC(17,16,10,10),
    VAMPIRE(17,16,10,10),
    SLIME_BOSS(200,300,10,30);

    public final float width;
    public final float height;
    public final float offsetX;
    public final float offsetY;

    EnemyHitbox(float width, float height, float offsetX, float offsetY) {
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
