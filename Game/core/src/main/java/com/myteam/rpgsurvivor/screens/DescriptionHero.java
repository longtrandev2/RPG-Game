package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

public class DescriptionHero implements Screen {
    private final Main game;
    private final ChosseHeroScreen previousScreen;
    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private Player player;
    private String heroType;
    private HeroType heroDescription;
    private GameScreen gameScreen;

    private Texture background;

    private Texture backButtonActive;
    private Texture backButtonInactive;
    private ImageButton backButton;
    private Texture playButtonActive;
    private Texture playButtonInactive;
    private ImageButton playButton;

    private Texture heroAvatar;

    private BitmapFont font;
    private Label.LabelStyle description;

    private float padding = 10;

    public DescriptionHero(Main game, ChosseHeroScreen previousScreen, OrthographicCamera camera, String heroType) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.camera = camera;
        this.heroType = heroType;
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
        this.gameScreen = new GameScreen(game, heroType);

        createFont();
        loadTextures();
        setupUI();
        createButtons();
        checkType(heroType);



        Gdx.input.setInputProcessor(stage);
    }

    private void loadTextures() {
        try {
            background = new Texture("Menu/Description hero/Description_hero.png");
            backButtonActive = new Texture("Menu/ChossenHero/Back_ButtonActive.png");
            backButtonInactive = new Texture("Menu/ChossenHero/Back_ButtonUnActive.png");
            playButtonActive = new Texture("Menu/Description hero/PlayBtnActive (1).png");
            playButtonInactive = new Texture("Menu/Description hero/PlayBtnUnActive (1).png");

            switch (heroType) {
                case "Knight":
                    heroAvatar = new Texture("Menu/IngameIcon/knightAva.png");
                    break;
                case "Samurai":
                    heroAvatar = new Texture("Menu/IngameIcon/SamuraiAva.png");
                    break;
                case "Archer":
                    heroAvatar = new Texture("Menu/IngameIcon/archerAva.png");
                    break;
                case "Wizard":
                    heroAvatar = new Texture("Menu/IngameIcon/wizardAva.png");
                    break;
                default:
                    heroAvatar = new Texture("Menu/IngameIcon/knightAva.png");
                    break;
            }
        } catch (Exception e) {
            Gdx.app.error("DescriptionHero", "Error loading textures", e);
        }
    }

    private void setupUI() {
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);
        stage.addActor(rootStack);

        Image backgroundImage = new Image(background);
        rootStack.add(backgroundImage);

    }

    private void createButtons() {

        Image avatar = new Image(heroAvatar);
        avatar.setScale(4);
        avatar.setPosition(Gdx.graphics.getWidth() / 2 - 400, Gdx.graphics.getHeight() / 2 - 50);

        TextureRegionDrawable backInactiveDrawable = new TextureRegionDrawable(backButtonInactive);
        TextureRegionDrawable backActiveDrawable = new TextureRegionDrawable(backButtonActive);

        backButton = new ImageButton(backInactiveDrawable, backActiveDrawable);
        backButton.setPosition(padding + 95, Gdx.graphics.getHeight() - backButton.getHeight() - padding - 115);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("DescriptionHero", "Back button clicked");
                game.setScreen(previousScreen);
            }
        });

        TextureRegionDrawable playInactiveDrawable = new TextureRegionDrawable(playButtonInactive);
        TextureRegionDrawable playActiveDrawable = new TextureRegionDrawable(playButtonActive);

        playButton = new ImageButton(playInactiveDrawable, playActiveDrawable);
        playButton.setPosition(
            Gdx.graphics.getWidth() - playButton.getWidth() - padding - 100,
            padding + 110
        );

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("DescriptionHero", "Play button clicked with hero: " + heroType);
                game.setScreen(new GameScreen(game, heroType));
            }
        });

        stage.addActor(backButton);
        stage.addActor(playButton);
        stage.addActor(avatar);
    }

    public void createFont()
    {
        try
        {
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
    public void setupLabel(int hp, int atk, float speed, float range)
    {
        Label hpLabel = new Label("" + hp, description);
        Label atkLabel = new Label("" + atk, description);
        Label speedLabel = new Label("" + speed, description);
        Label rangeLabel = new Label("" + range, description);

        float paddingX = 400;
        hpLabel.setPosition(Gdx.graphics.getWidth() / 2 + paddingX, Gdx.graphics.getHeight() / 2 + 50);
        atkLabel.setPosition(Gdx.graphics.getWidth() / 2 + paddingX, Gdx.graphics.getHeight() / 2 - 20);
        speedLabel.setPosition(Gdx.graphics.getWidth() / 2 + paddingX, Gdx.graphics.getHeight() / 2 - 90);
        rangeLabel.setPosition(Gdx.graphics.getWidth() / 2 + paddingX, Gdx.graphics.getHeight() / 2 - 150);


        stage.addActor(hpLabel);
        stage.addActor(atkLabel);
        stage.addActor(speedLabel);
        stage.addActor(rangeLabel);
    }

    public void checkType(String heroType)
    {
        switch (heroType) {
            case "Knight":
                setupLabel(HeroType.KNIGHT.stat.maxHealth, HeroType.KNIGHT.stat.damage, HeroType.KNIGHT.stat.moveSpeed, HeroType.KNIGHT.stat.rangeAttack);
                break;
            case "Samurai":
                setupLabel(HeroType.SAMURAI.stat.maxHealth, HeroType.SAMURAI.stat.damage, HeroType.SAMURAI.stat.moveSpeed, HeroType.SAMURAI.stat.rangeAttack);
                break;
            case "Archer":
                setupLabel(HeroType.ARCHER.stat.maxHealth, HeroType.ARCHER.stat.damage, HeroType.ARCHER.stat.moveSpeed, HeroType.ARCHER.stat.rangeAttack);
                break;
            case "Wizard":
                setupLabel(HeroType.WIZARD.stat.maxHealth, HeroType.WIZARD.stat.damage, HeroType.WIZARD.stat.moveSpeed, HeroType.WIZARD.stat.rangeAttack);
                break;
            default:
                setupLabel(HeroType.KNIGHT.stat.maxHealth, HeroType.KNIGHT.stat.damage, HeroType.KNIGHT.stat.moveSpeed, HeroType.KNIGHT.stat.rangeAttack);
                break;
        }
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
        background.dispose();
        backButtonActive.dispose();
        backButtonInactive.dispose();
        playButtonActive.dispose();
        playButtonInactive.dispose();
    }
}
