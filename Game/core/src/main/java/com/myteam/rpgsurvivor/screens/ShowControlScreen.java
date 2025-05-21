package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;

public class ShowControlScreen implements Screen {
    private final Main game;
    private final PauseScreen previousScreen;
    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private boolean isShow;

    private Texture background;

    private Texture backUnActiveTexture;
    private Texture backActiveTexture;
    private ImageButton backBtn;

    public ShowControlScreen(Main game, PauseScreen previousScreen, OrthographicCamera camera)
    {
        this.game = game;
        this.previousScreen = previousScreen;
        this.camera = camera;
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
        isShow = false;

        loadTexture();
        createUI(); // Add this line to call createUI in the constructor
    }

    public void loadTexture() {
        try
        {
            background = new Texture(Gdx.files.internal("Menu/PauseMenu/controlScreen.png"));
            backUnActiveTexture = new Texture(Gdx.files.internal("Menu/ChossenHero/Back_ButtonUnActive.png"));
            backActiveTexture = new Texture(Gdx.files.internal("Menu/ChossenHero/Back_ButtonActive.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createUI()
    {
        TextureRegionDrawable backUnActiveDrawable = new TextureRegionDrawable(backUnActiveTexture);
        TextureRegionDrawable backActiveDrawable = new TextureRegionDrawable(backActiveTexture);
        backBtn = new ImageButton(backUnActiveDrawable, backActiveDrawable);


        backBtn.setPosition(Gdx.graphics.getWidth() / 2 - 210, Gdx.graphics.getHeight() / 2 + 230);
        backBtn.setSize(100, 50);

        backBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("DescriptionHero", "Back button clicked");
                isShow = false;
            }
        });

        stage.addActor(backBtn);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (isShow) {
            Gdx.input.setInputProcessor(stage);
            stage.act(delta);

            batch.begin();


            float bgWidth = background.getWidth() * 2;
            float bgHeight = background.getHeight() * 2;
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();

            batch.draw(background,
                (screenWidth - bgWidth) / 2,
                (screenHeight - bgHeight) / 2,
                bgWidth, bgHeight);

            batch.end();
            stage.draw();
        }
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
        background.dispose();
        backUnActiveTexture.dispose();
        backActiveTexture.dispose();
    }

    public boolean isShow()
    {
        return isShow;
    }

    public void setShow(boolean isShow)
    {
        this.isShow = isShow;
        if (isShow) {
            Gdx.input.setInputProcessor(stage);
        }
    }
}
