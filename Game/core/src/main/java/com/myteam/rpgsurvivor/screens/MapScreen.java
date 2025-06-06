package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.controller.system.AudioManager;
import com.myteam.rpgsurvivor.controller.system.SystemController;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.impl.Hero.Archer;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;
import com.myteam.rpgsurvivor.model.impl.Hero.Wizard;
import com.myteam.rpgsurvivor.saveGame.GameSaveData;
import com.myteam.rpgsurvivor.saveGame.GameSaveManager;

public class MapScreen implements Screen {
    private Main game;
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player chosenHero;
    private LayoutPlayScreen layoutPlayScreen;
    private EnemySpawnController enemySpawnController;
    private SystemController systemController;

    private AudioManager audioManager;

    private boolean debugEnabled = false;

    private String heroType;

    private GameSaveManager gameSaveManager;
    private GameSaveData gameSaveData;

    private boolean isSaving;
    private float saveTimer = 0f;
    private static final float SAVE_INTERVAL = 2.0f;


    public MapScreen(String heroType, Main game) {
        this.game = game;
        this.heroType = heroType;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        isSaving = false;
        audioManager = AudioManager.getInstance();


        try {
            batch = new SpriteBatch();

            switch (heroType)
            {
                case "Knight" :
                    chosenHero = new Knight(400, 400);
                    break;
                case "Samurai" :
                    chosenHero = new Samurai(400,400);
                    break;
                case "Archer" :
                    chosenHero = new Archer(400, 400);
                    break;
                case "Wizard" :
                    chosenHero = new Wizard(400, 400);
                    break;
                default:
                    chosenHero = new Knight(400,400);
                    break;
            }

        } catch (Exception e) {
            Gdx.app.error("MapScreen", "Error initializing: " + e.getMessage());
            e.printStackTrace();
        }


        loadMap();
        enemySpawnController = new EnemySpawnController(chosenHero, map);

        systemController = new SystemController(enemySpawnController, chosenHero, game, this, camera);

        enemySpawnController.setMaxEnemiesOnMap(5);
        enemySpawnController.setSpawnInterval(3.0f);
        enemySpawnController.setTimeBetweenWaves(45.0f);

        chosenHero.setEnemySpawnController(enemySpawnController);

        layoutPlayScreen = new LayoutPlayScreen(camera,chosenHero,heroType,game);

        gameSaveManager = new GameSaveManager();
    }

    public void loadMap() {
        try {
            map = new TmxMapLoader().load("Map Asset/Map Final.tmx");
            tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        } catch (Exception e) {
            Gdx.app.error("Map Loading", "Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {
        camera.update();
        if (!isPaused()) {
            chosenHero.update(Gdx.graphics.getDeltaTime());
            systemController.update(Gdx.graphics.getDeltaTime());
        }
        else {
                if(isSaving == false)
                {
                    saveGame();
                    isSaving = true;
                }
        }

    }

    private void saveGame() {
        try {
            gameSaveData = new GameSaveData(this);
            System.out.println(this);
            gameSaveManager.saveGame(gameSaveData);
            Gdx.app.log("MapScreen", "Game saved successfully");
            System.out.println(gameSaveData.toString());
        } catch (Exception e) {
            Gdx.app.error("MapScreen", "Error saving game: " + e.getMessage());
            e.printStackTrace();
        } finally {
            isSaving = false;
        }


    }


    @Override
    public void show() {
        if(!isPaused())
        {
            audioManager.playGameMusic();
        }
        else
        {
            audioManager.pauseMusic();
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            DebugRenderer.setEnabled(!debugEnabled);
            debugEnabled = !debugEnabled;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (tiledMapRenderer != null) {
            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();
        }

        batch.begin();

        if(chosenHero.isDead())
        {
            layoutPlayScreen.render(Gdx.graphics.getDeltaTime());
        }
        else
        {
            layoutPlayScreen.render(Gdx.graphics.getDeltaTime());
            if (!isPaused()) {
                if (enemySpawnController != null && !systemController.isWaitingForNextStage()) {
                    chosenHero.render(batch, Gdx.graphics.getDeltaTime());
                    boolean isBossWave = enemySpawnController.isBossWave();
                    //System.out.println(isBossWave);
                    if (isBossWave) {
                        enemySpawnController.renderBoss(batch, Gdx.graphics.getDeltaTime());
                    } else {
                        enemySpawnController.renderCreep(batch, Gdx.graphics.getDeltaTime());
                    }
                } else {
                    systemController.render(Gdx.graphics.getDeltaTime());
                }
            }
        }

        batch.end();
        DebugRenderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
        if (map != null) {
            map.dispose();
        }
        batch.dispose();
        layoutPlayScreen.dispose();
        systemController.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public boolean isPaused() {
        return layoutPlayScreen != null && layoutPlayScreen.isPaused();
    }

    public Main getGame() {
        return game;
    }

    public Player getChosenHero() {
        return chosenHero;
    }

    public EnemySpawnController getEnemySpawnController() {
        return enemySpawnController;
    }

    public SystemController getSystemController() {
        return systemController;
    }

    public void setChosenHero(Player chosenHero) {
        this.chosenHero = chosenHero;
    }

    public LayoutPlayScreen getLayoutPlayScreen() {
        return layoutPlayScreen;
    }

    public void setLayoutPlayScreen(LayoutPlayScreen layoutPlayScreen) {
        this.layoutPlayScreen = layoutPlayScreen;
    }
}
