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
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.model.Player;

public class ChosseHeroScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player chosenHero;
    private int w;
    private int h;

    // Background Choose Hero
    private Texture BGTexture;
    private Texture backBtnUnActive;
    private Texture backBtnActive;
    private ImageButton backBtn;

    // Frame Hero and Avatar Hero;
    private Texture frameHero;
    private Texture knightAva;
    private Texture samuraiAva;
    private Texture archerAva;
    private Texture wizardAva;

    private float avatarFrameSize = 150;
    private float padding = 10;

    public ChosseHeroScreen(final Main game) {
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
        createBackButton();

        Gdx.input.setInputProcessor(stage);
    }

    public void loadTextures() {
        BGTexture = new Texture("Menu/ChossenHero/BG 4 nhân vật.png");
        frameHero = new Texture("Menu/IngameIcon/Khung ava.png");
        knightAva = new Texture("Menu/IngameIcon/knightAva.png");
        samuraiAva = new Texture("Menu/IngameIcon/SamuraiAva.png");
        archerAva = new Texture("Menu/IngameIcon/archerAva.png");
        wizardAva = new Texture("Menu/IngameIcon/wizardAva.png");
        backBtnActive = new Texture("Menu/ChossenHero/Back_ButtonActive.png");
        backBtnUnActive = new Texture("Menu/ChossenHero/Back_ButtonUnActive.png");
    }

    public void setupLayout() {
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);
        stage.addActor(rootStack);

        Image background = new Image(BGTexture);
        rootStack.add(background);

        Table heroTable = new Table();
        heroTable.setFillParent(true);

        Table knightTable = createHeroCell(knightAva, "Knight");
        Table samuraiTable = createHeroCell(samuraiAva, "Samurai");
        Table archerTable = createHeroCell(archerAva, "Archer");
        Table wizardTable = createHeroCell(wizardAva, "Wizard");

        heroTable.add(knightTable).pad(50).size(avatarFrameSize * 1.5f);
        heroTable.add(samuraiTable).pad(50).size(avatarFrameSize * 1.5f);
        heroTable.add(archerTable).pad(50).size(avatarFrameSize * 1.5f);
        heroTable.add(wizardTable).pad(50).size(avatarFrameSize * 1.5f);

        stage.addActor(heroTable);
    }

    private Table createHeroCell(final Texture heroTexture, final String heroType) {
        Table cell = new Table();

        Image heroAvatar = new Image(heroTexture);
        Image frame = new Image(frameHero);

        heroAvatar.setSize(avatarFrameSize * 0.8f, avatarFrameSize * 0.8f);
        frame.setSize(avatarFrameSize, avatarFrameSize);

        Stack heroStack = new Stack();
        heroStack.add(frame);
        heroStack.add(heroAvatar);

        cell.add(heroStack).size(avatarFrameSize);

        cell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try
                {
                    Gdx.app.log("ChosseHeroScreen", heroType + " selected");
                    game.setScreen(new DescriptionHero(game, ChosseHeroScreen.this, camera, heroType));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return cell;
    }

    public void createBackButton() {
        TextureRegionDrawable backBtnUnActiveDrawable = new TextureRegionDrawable(backBtnUnActive);
        TextureRegionDrawable backBtnActiveDrawable = new TextureRegionDrawable(backBtnActive);

        backBtn = new ImageButton(backBtnUnActiveDrawable, backBtnActiveDrawable);
        backBtn.setPosition(padding, h - backBtn.getHeight() - padding);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ChosseHeroScreen", "Back button clicked");
                game.setScreen(new MainMenuScreen(game));
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        BGTexture.dispose();
        frameHero.dispose();
        knightAva.dispose();
        samuraiAva.dispose();
        archerAva.dispose();
        wizardAva.dispose();
        backBtnUnActive.dispose();
        backBtnActive.dispose();
    }
}
