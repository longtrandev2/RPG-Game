package com.myteam.rpgsurvivor.controller.spawn;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PointSpawnArea implements SpawnArea{

    private float x, y;
    private static final float POINT_VARIANCE = 10f; // Một chút biến thiên để tránh spawn cùng một vị trí

    public PointSpawnArea(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2 getRandomPosition() {
        float offsetX = MathUtils.random(-POINT_VARIANCE, POINT_VARIANCE);
        float offsetY = MathUtils.random(-POINT_VARIANCE, POINT_VARIANCE);
        return new Vector2(x + offsetX, y + offsetY);
    }
}
