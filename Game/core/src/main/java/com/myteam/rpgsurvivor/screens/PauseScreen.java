package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;

public class PauseScreen implements Screen {
    private Main game;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private ShowControlScreen showControlScreen;
    private MainMenuScreen mainMenuScreen;

    private Texture backgroundTexture;


    private Texture controlUnActiveTexture;
    private Texture controlActiveTexture;
    private ImageButton controlBtn;


    private Texture audioUnActiveTexure;
    private Texture audioActiveTexture;
    private ImageButton audioBtn;

    private Texture exitUnActiveTexture;
    private Texture exitActiveTexture;
    private ImageButton exitBtn;

    private Texture resumeUnActiveTexture;
    private Texture resumeActiveTexture;
    private ImageButton resumeBtn;



    //Interface resolve event
    private PauseScreenListener listener ;

    // Size parameters
    private float padding = 20;
    private float buttonSize = 200;
    private float titleWidth = 800;
    private float titleHeight = 200;

    public PauseScreen (OrthographicCamera camera, Main game)
    {
        this.game = game;
        this.camera = camera;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        showControlScreen = new ShowControlScreen(game, PauseScreen.this,camera);
        mainMenuScreen = new MainMenuScreen(game);

        try {
            backgroundTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/BackGround.png"));
            controlUnActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/controlUnActive.png"));
            controlActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/controlActive.png"));
            audioUnActiveTexure = new Texture(Gdx.files.internal("Menu/PauseMenu/audioUnActive.png"));
            audioActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/audioActive.png"));
            exitUnActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/exitUnActive.png"));
            exitActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/exitActive.png"));
            resumeUnActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/resumeUnActive.png"));
            resumeActiveTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/resumeActive.png"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        createMenu();

        Gdx.input.setInputProcessor(stage);
    }
    public ImageButton createButton(Texture unactiveBtn, Texture activeBtn)
    {
        TextureRegionDrawable unactiveDrawable = new TextureRegionDrawable(unactiveBtn);
        TextureRegionDrawable activeDrawable = new TextureRegionDrawable(activeBtn);
        ImageButton button = new ImageButton(unactiveDrawable,activeDrawable);
        return button;
    }
    public void createMenu()
    {
        controlBtn = createButton(controlUnActiveTexture, controlActiveTexture);
        audioBtn = createButton(audioUnActiveTexure, audioActiveTexture);
        exitBtn = createButton(exitUnActiveTexture, exitActiveTexture);
        resumeBtn = createButton(resumeUnActiveTexture,resumeActiveTexture);

        controlBtn.setPosition(Gdx.graphics.getWidth() / 2 - 280, Gdx.graphics.getHeight() / 2 -  100);
        audioBtn.setPosition(Gdx.graphics.getWidth() / 2 + 100, Gdx.graphics.getHeight() / 2 - 100);
        exitBtn.setPosition(Gdx.graphics.getWidth() / 2 - 280, Gdx.graphics.getHeight() / 2 - 200);
        resumeBtn.setPosition(Gdx.graphics.getWidth() / 2 - 30, Gdx.graphics.getHeight() / 2);
        resumeBtn.setScale(4);

        controlBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("DescriptionHero", "Control button clicked");
                showControlScreen.setShow(true);

            }
        });

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onBackButtonClicked();
                }
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               game.setScreen(mainMenuScreen);
            }
        });

        stage.addActor(controlBtn);
        stage.addActor(audioBtn);
        stage.addActor(exitBtn);
        stage.addActor(resumeBtn);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float bgWidth = screenWidth * 0.8f;
        float bgHeight = screenHeight * 0.8f;
        float bgX = (screenWidth - bgWidth) / 2;
        float bgY = (screenHeight - bgHeight) / 2;

        batch.draw(backgroundTexture, bgX, bgY, bgWidth, bgHeight);



        batch.end();
        stage.draw();

        if(showControlScreen.isShow())
        {
            showControlScreen.render(delta);
        }
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

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
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        controlUnActiveTexture.dispose();
        controlActiveTexture.dispose();
        audioUnActiveTexure.dispose();
        audioUnActiveTexure.dispose();

    }

    public void setListener(PauseScreenListener listener)
    {
        this.listener = listener;
    }


}
