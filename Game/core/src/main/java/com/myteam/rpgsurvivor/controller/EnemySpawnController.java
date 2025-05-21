package com.myteam.rpgsurvivor.controller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.controller.spawn.SpawnPointManager;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.BossType;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.impl.Boss.SlimeBoss;
import com.myteam.rpgsurvivor.model.impl.Creep.*;


import java.util.ArrayList;

public class EnemySpawnController {
    private SpawnPointManager spawnPointManager;
    private ArrayList<Enemy> activeEnemy;
    private ArrayList<Enemy> activeBoss;
    private Player player;
    private AnimationForEnemy enemyAnimation;

    private int totalDeath = 0;
    private float spawnInterval;
    private float spawnTimer;
    private int maxEnemiesOnMap;
    private int enemiesPerWave;

    private int currentWave;
    private float waveTimer;
    private float timeBetweenWaves;

    private boolean prepareToNextStage;
    private boolean isPaused;
    private boolean isBossWave;
    private boolean bossSpawned;

    public EnemySpawnController(Player player, TiledMap map) {
        this.player = player;
        this.spawnPointManager = new SpawnPointManager();
        this.spawnPointManager.loadFromMap(map);
        this.activeEnemy = new ArrayList<>();
        this.activeBoss = new ArrayList<>();
        this.enemyAnimation = new AnimationForEnemy();

        this.spawnInterval = 2.0f;
        this.spawnTimer = 0;
        this.maxEnemiesOnMap = 100;
        this.enemiesPerWave = 5;

        this.currentWave = 1;
        this.waveTimer = 0;
        this.timeBetweenWaves = 30.0f;

        this.prepareToNextStage = false;
        this.isPaused = false;
        this.isBossWave = (currentWave % 5 == 0);
        this.bossSpawned = false;
    }

    public void update(float deltaTime) {
//        System.out.println(currentWave);
//        System.out.println(isBossWave);
        if (isPaused) {
            return;
        }

        if (totalDeath >= enemiesPerWave && !isBossWave) {
            System.out.println("Regular wave completed - Deaths: " + totalDeath);
            prepareToNextStage = true;
            return;
        }

        if (isBossWave && activeBoss.isEmpty() && bossSpawned) {
            System.out.println("Boss wave completed");
            prepareToNextStage = true;
            return;
        }

        spawnTimer += deltaTime;

        if (!isBossWave) {
            if (spawnTimer >= spawnInterval && activeEnemy.size() < maxEnemiesOnMap) {
                spawnEnemy();
                spawnTimer = 0;
            }
            updateEnemy(deltaTime);
        }
        else {
            if (!bossSpawned) {
                spawnBoss();
                bossSpawned = true;
                spawnTimer = 0;
            }
            updateBoss(deltaTime);
        }
    }

    public void spawnEnemy() {
        Vector2 spawnPos = spawnPointManager.getRandomSpawnPosition();
        Enemy enemy = createRandomEnemy(spawnPos.x, spawnPos.y);
        activeEnemy.add(enemy);
    }

    public void spawnBoss() {
        Vector2 spawnPos = spawnPointManager.getSpawnBossPosition();
        Enemy enemy = createBoss(spawnPos.x, spawnPos.y);
        activeBoss.add(enemy);
    }

    public Enemy createRandomEnemy(float x, float y) {
        MonsterType[] types = MonsterType.values();
        MonsterType randomType = types[MathUtils.random(types.length - 1)];
//        MonsterType randomType = MonsterType.VAMPIRE;

        Enemy randomEnemy = createEnemyByType(randomType, x, y);
        return randomEnemy;
    }

    public Enemy createBoss(float x, float y) {
        BossType[] types = BossType.values();
        BossType randomType = BossType.SLIME_BOSS;

        Enemy boss = createBossByType(randomType, x, y);
        return boss;
    }

    private Enemy createBossByType(BossType type, float x, float y) {
        switch (type) {
            case SLIME_BOSS:
                return new SlimeBoss(x,y,player,enemyAnimation);
            default:
                return new SlimeBoss(x, y, player, enemyAnimation);
        }
    }

    private Enemy createEnemyByType(MonsterType type, float x, float y) {
        switch (type) {
            case GOBLIN:
                return new Goblin(x, y, player, enemyAnimation);
            case SKELETON:
                return new Skeleton(x, y, player, enemyAnimation);
            case RAT:
                return new Rat(x, y, player, enemyAnimation);
            case ORC:
                return new Orc(x, y, player, enemyAnimation);
            case VAMPIRE:
                return new Vampire(x, y, player, enemyAnimation);
            default:
                return new Goblin(x, y, player, enemyAnimation);
        }
    }

    private void updateEnemy(float deltaTime) {
        for (int i = activeEnemy.size() - 1; i >= 0; i--) {
            Enemy enemy = activeEnemy.get(i);

            enemy.update(deltaTime);

            if (enemy.isDead()) {
                activeEnemy.remove(i);
                totalDeath++;
                if (totalDeath >= enemiesPerWave) {
                    prepareToNextStage = true;
                }
            }
        }
    }

    private void updateBoss(float deltaTime) {
        for (int i = activeBoss.size() - 1; i >= 0; i--) {
            Enemy enemy = activeBoss.get(i);

            enemy.update(deltaTime);

            if (enemy.isDead()) {
                activeBoss.remove(i);
            }
        }

    }

    public void renderCreep(SpriteBatch batch, float deltaTime) {
        for (Enemy enemy : activeEnemy) {
            enemy.render(batch, deltaTime);
        }
    }

    public void renderBoss(SpriteBatch batch, float deltaTime) {
        for (Enemy enemy : activeBoss) {
            enemy.render(batch, deltaTime);
        }
    }

    public boolean isPrepareToNextStage() {
        return prepareToNextStage;
    }

    public void pauseSpawning() {
        isPaused = true;
    }

    public void resumeSpawning() {
        prepareToNextStage = false;
        isPaused = false;
        currentWave++;
        totalDeath = 0;
        isBossWave = currentWave % 5 == 0;
        bossSpawned = false;
        activeEnemy.clear();
        activeBoss.clear();
    }

    public ArrayList<Enemy> getActiveEnemies() {
        return activeEnemy;
    }

    public ArrayList<Enemy> getActiveBoss() {
        return activeBoss;
    }

    public void setMaxEnemiesOnMap(int maxEnemiesOnMap) {
        this.maxEnemiesOnMap = maxEnemiesOnMap;
    }

    public void setSpawnInterval(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    public void setTimeBetweenWaves(float timeBetweenWaves) {
        this.timeBetweenWaves = timeBetweenWaves;
    }

    public float getTimeBetweenWaves() {
        return timeBetweenWaves;
    }

    public int getMaxEnemiesOnMap() {
        return maxEnemiesOnMap;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getEnemiesPerWave() {
        return enemiesPerWave;
    }

    public void setEnemiesPerWave(int enemiesPerWave) {
        this.enemiesPerWave = enemiesPerWave;
    }

    public int getTotalDeaths() {
        return totalDeath;
    }

    public void setTotalDeaths(int totalDeath) {
        this.totalDeath = totalDeath;
    }

    public boolean isBossWave() {
        return isBossWave;
    }
}
