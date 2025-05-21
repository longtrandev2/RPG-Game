package com.myteam.rpgsurvivor.controller.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.screens.MapScreen;
import com.myteam.rpgsurvivor.screens.UpgradeScreen;
import com.myteam.rpgsurvivor.screens.UpgradeScreenListener;

public class SystemController implements Screen {
    private EnemySpawnController enemySpawnController;
    private OrthographicCamera camera;
    private Player player;
    private Main game;
    private MapScreen mapScreen;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private UpgradeScreen upgradeScreen;

    private Texture frameStage;

    private int currentStage;
    private float stageTimer;
    private float stageDuration;
    private boolean stageCompleted;

    private float stageTransitionTimer;
    private float stageTransitionDelay;
    private boolean isWaitingForNextStage;
    private boolean isShowingUpgradeScreen;

    private float difficulty;

    private BitmapFont font;
    private Label.LabelStyle description;

    private int healthMod = 0;
    private int damageMod = 0;
    private int speedMod = 0;
    private int atkSpeedMod = 0;
    private int skillMod = 0;
    private int availablePoints = 10;

    public SystemController(EnemySpawnController enemySpawnController, Player player, Main game, MapScreen mapScreen, OrthographicCamera camera) {
        this.enemySpawnController = enemySpawnController;
        this.player = player;
        this.game = game;
        this.mapScreen = mapScreen;
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
        this.upgradeScreen = new UpgradeScreen(camera, game, player);

        this.currentStage = enemySpawnController.getCurrentWave();
        this.stageTimer = 0f;
        this.stageDuration = 30f;
        this.stageCompleted = false;
        this.difficulty = 1f;

        this.stageTransitionTimer = 0f;
        this.stageTransitionDelay = 10f;
        this.isWaitingForNextStage = false;
        this.isShowingUpgradeScreen = false;

        frameStage = new Texture(Gdx.files.internal("Menu/NoticeStage/Frame.png"));

        createFont();
        setupLabel(currentStage);
    }

    public void createFont() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Menu/Font/antiquity-print.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter titleParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
            titleParams.size = 36;
            titleParams.color = Color.BLACK;

            font = generator.generateFont(titleParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        description = new Label.LabelStyle(font, Color.BLACK);
    }

    public void setupLabel(int currentStage) {
        Image stageFrame = new Image(frameStage);
        Label stageLabel = new Label("Stage " + currentStage , description);
        float labelWidth = stageLabel.getWidth();
        float labelHeight = stageLabel.getHeight();

        stageLabel.setPosition(
            (Gdx.graphics.getWidth() / 2f) - (labelWidth / 2f) - 5,
            (Gdx.graphics.getHeight() / 2f) - (labelHeight / 2f) + 320
        );

        stageFrame.setPosition(
            (Gdx.graphics.getWidth() / 2f) - (labelWidth / 2f) - 100,
            (Gdx.graphics.getHeight() / 2f) - (labelHeight / 2f) + 300
        );

        stage.addActor(stageFrame);
        stage.addActor(stageLabel);
    }

    public void update(float deltaTime) {
        if (isWaitingForNextStage) {
            stageTransitionTimer += deltaTime;
            if (upgradeScreen.isDone()) {
                startNextStage();
            }
            return;
        }

        enemySpawnController.update(deltaTime);
        stageTimer += deltaTime;

        if (enemySpawnController.isPrepareToNextStage() && !isWaitingForNextStage) {
            prepareForNextStage();
        }

        updateDifficulty();
    }


    private void prepareForNextStage() {
        stageCompleted = true;
        isWaitingForNextStage = true;
        stageTransitionTimer = 0f;
        enemySpawnController.pauseSpawning();
        showUpgradeScreen();
        System.out.println("Stage " + currentStage + " cleared. Preparing for next stage.");
    }

    private void showUpgradeScreen() {
        isShowingUpgradeScreen = true;
        Gdx.input.setInputProcessor(upgradeScreen.getStage());
        upgradeScreen.show();
        upgradeScreen.reset();
    }

    private void startNextStage() {
        currentStage++;
        stageTimer = 0f;
        isShowingUpgradeScreen = false;
        stageCompleted = false;
        isWaitingForNextStage = false;
        player.addSkillPoints(10);
        enemySpawnController.setTotalDeaths(0);
        enemySpawnController.resumeSpawning();

        setupLabel(currentStage);
        adjustEnemySpawn();

        System.out.println("Starting Stage " + currentStage);
    }

    private void adjustEnemySpawn() {
        int currentMaxEnemies = enemySpawnController.getEnemiesPerWave();
        enemySpawnController.setEnemiesPerWave(Math.min(currentMaxEnemies + 5, 100));

        float currentSpawnInterval = enemySpawnController.getSpawnInterval();
        enemySpawnController.setSpawnInterval(Math.max(currentSpawnInterval * 0.9f, 0.5f));

        float currentTimeBetweenWaves = enemySpawnController.getTimeBetweenWaves();
        enemySpawnController.setTimeBetweenWaves(Math.max(currentTimeBetweenWaves * 0.9f, 15f));
    }

    private void updateDifficulty() {
        difficulty = 1f + (currentStage - 1) * 0.2f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(isShowingUpgradeScreen)
        {
            upgradeScreen.render(delta);
        }

        stage.act(delta);
        stage.draw();


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

    @Override
    public void dispose() {
        stage.dispose();

        if (upgradeScreen != null) {
            upgradeScreen.dispose();
        }

        if (frameStage != null) {
            frameStage.dispose();
        }

        if (game != null) {
            game.dispose();
        }

        if (camera != null) {
            camera = null;
        }

    }

    public boolean isWaitingForNextStage() {
        return isWaitingForNextStage;
    }
}

