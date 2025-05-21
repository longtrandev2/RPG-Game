package com.myteam.rpgsurvivor.controller.spawn;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class SpawnPointManager {
    private ArrayList<SpawnArea> spawnAreas;
    private ArrayList<SpawnArea> spawnBoss;

    public SpawnPointManager() {
        spawnAreas = new ArrayList<>();
        spawnBoss = new ArrayList<>();
    }

    public void loadFromMap(TiledMap map) {
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().startsWith("Khu Spawn")) {
                MapObjects objects = layer.getObjects();
                for (MapObject object : objects) {
                    float x = object.getProperties().get("x", Float.class);
                    float y = object.getProperties().get("y", Float.class);
                    spawnAreas.add(new PointSpawnArea(x, y));
                }
            }
            else if (layer.getName().startsWith("Khu Boss")) {
                MapObjects objects = layer.getObjects();
                for (MapObject object : objects) {
                    float x = object.getProperties().get("x", Float.class);
                    float y = object.getProperties().get("y", Float.class);
                    spawnBoss.add(new PointSpawnArea(x, y));
                }
            }
        }
    }


    public Vector2 getRandomSpawnPosition() {
        if (spawnAreas.isEmpty()) {
            return new Vector2(0, 0);
        }

        int randomIndex = MathUtils.random(spawnAreas.size() - 1);
        return spawnAreas.get(randomIndex).getRandomPosition();
    }

    public Vector2 getSpawnBossPosition()
    {
        if (spawnAreas.isEmpty()) {
            return new Vector2(0, 0);
        }
        return spawnBoss.get(0).getRandomPosition();
    }

    public Vector2 getSpawnPositionFromArea(int areaIndex) {
        if (areaIndex >= 0 && areaIndex < spawnAreas.size()) {
            return spawnAreas.get(areaIndex).getRandomPosition();
        }
        return getRandomSpawnPosition();
    }

    public int getSpawnAreaCount() {
        return spawnAreas.size();
    }
}
