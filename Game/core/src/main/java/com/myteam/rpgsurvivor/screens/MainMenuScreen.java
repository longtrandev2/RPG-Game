package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;

public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float w;
    private final float h;

    // Background
    private Texture backGDTexture;
    private Texture logoGame;

    // Button
    private Texture playUnHover;
    private Texture playHover;
    private ImageButton playButton;

    private Texture settingUnHover;
    private Texture settingHover;
    private ImageButton settingButton;

    private Texture exitUnHover;
    private Texture exitHover;
    private ImageButton exitButton;


    private int btnSizeWidth = 100;
    private int btnSizeHeight = 50;
    private int paddingY = 20;
    private int paddingX = 100;

    public MainMenuScreen(final Main game) {
        this.game = game;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        viewport = new FitViewport(w, h, camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);


        loadTextures();
        setupLayout();

        Gdx.input.setInputProcessor(stage);
    }

    private void loadTextures() {
        backGDTexture = new Texture(Gdx.files.internal("Menu/IntroScreen/BG.png"));
        logoGame = new Texture(Gdx.files.internal("Menu/IntroScreen/LogoGame.png"));

        playUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/PlayIcon.png"));
        playHover = new Texture(Gdx.files.internal("Menu/IntroScreen/PlayIconHover.png"));

        settingUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/SettingIcon.png"));
        settingHover = new Texture(Gdx.files.internal("Menu/IntroScreen/SettingIconHover.png"));

        exitUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/ExitIconUnHover.png"));
        exitHover = new Texture(Gdx.files.internal("Menu/IntroScreen/ExitIconHover.png"));

    }

    private void setupLayout() {
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);
        stage.addActor(rootStack);

        Image background = new Image(backGDTexture);
        rootStack.add(background);

        TextureRegionDrawable playUnAC = new TextureRegionDrawable(playUnHover);
        TextureRegionDrawable playAC = new TextureRegionDrawable(playHover);
        playButton = new ImageButton(playUnAC, playAC);

        TextureRegionDrawable settingUnAC = new TextureRegionDrawable(settingUnHover);
        TextureRegionDrawable settingAC = new TextureRegionDrawable(settingHover);
        settingButton = new ImageButton(settingUnAC, settingAC);

        TextureRegionDrawable exitUnAC = new TextureRegionDrawable(exitUnHover);
        TextureRegionDrawable exitAC = new TextureRegionDrawable(exitHover);
        exitButton = new ImageButton(exitUnAC, exitAC);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenuScreen", "Play button clicked");
                game.setScreen(new ChosseHeroScreen(game));

            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenuScreen", "Settings button clicked");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenuScreen", "Exit button clicked");
                Gdx.app.exit();
            }
        });

        Image logo = new Image(logoGame);
        float logoScale = 0.4f;
        logo.setScale(logoScale);

        logo.setPosition(400, 250);
        stage.addActor(logo);



        playButton.setSize(btnSizeWidth, btnSizeHeight);
        playButton.setPosition(560, 300);
        stage.addActor(playButton);

        settingButton.setSize(btnSizeWidth, btnSizeHeight);
        settingButton.setPosition(560, 200);
        stage.addActor(settingButton);

        exitButton.setSize(btnSizeWidth, btnSizeHeight);
        exitButton.setPosition(560, 100);
        stage.addActor(exitButton);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backGDTexture.dispose();
        logoGame.dispose();
        playUnHover.dispose();
        playHover.dispose();
        settingUnHover.dispose();
        settingHover.dispose();
        exitUnHover.dispose();
        exitHover.dispose();
    }
}
