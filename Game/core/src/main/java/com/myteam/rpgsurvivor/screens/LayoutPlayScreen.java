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
import com.myteam.rpgsurvivor.model.Player;

public class LayoutPlayScreen implements Screen {
    private Main game;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private Player chosenHero;
    private PauseScreen pauseScreen;
    private String heroType;

    //Pause Button
    private Texture pauseUnactiveTexture;
    private Texture pauseActiveTexture;
    private ImageButton pauseButton;

    // UI Interface blood  and hero avatar
    private Texture heroAvatar;
    private Texture frameAvatar;
    private Texture frameBlood;
    private Texture bloodTexture;

    private TextureRegion fullBloodFrame;

    private float avatarFrameSize = 80;
    private float bloodBarWidth = 200;
    private float bloodBarHeight = 40;
    private float padding = 10;
    private float bloodBarInnerPaddingX = 10;
    private float bloodBarInnerPaddingY = 10;

    private float maxHealth ;
    private float currentHealth ;

    private static final int BLOOD_FRAME_COLS = 6;
    private static final int BLOOD_FRAME_ROWS = 1;

    private boolean isPaused = false;


    //Kích thước thanh máu và frame máu
    float innerBloodWidth = bloodBarWidth - (2 * bloodBarInnerPaddingX)  + 15 ;
    float innerBloodHeight = bloodBarHeight - (2 * bloodBarInnerPaddingY);

    float topY = Gdx.graphics.getHeight() - padding - avatarFrameSize;

    float avatarPadding = 5;
    float avatarSize = avatarFrameSize - (avatarPadding * 2);
    //Vị trí khung máu
    float bloodBarX = padding + avatarFrameSize + padding;
    float bloodBarY = topY + (avatarFrameSize - bloodBarHeight) / 2;
    public LayoutPlayScreen(OrthographicCamera camera, Player chosenHero, String heroType, Main game)
    {
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        this.heroType = heroType;
        this.chosenHero = chosenHero;
        maxHealth = chosenHero.getMaxHealth();
        currentHealth = chosenHero.getCurrentHealth();
        pauseScreen = new PauseScreen(camera,game);
        pauseScreen.setListener(new PauseScreenListener() {
            @Override
            public void onBackButtonClicked() {
                System.out.println("Home button clicked");
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void onResumeButtonClicked() {
                System.out.println("Resume button clicked");
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void onRestartButtonClicked() {
                System.out.println("Restart button clicked");
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }




        });

        try {
            pauseUnactiveTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/pauseGameUnactive.png"));
            pauseActiveTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/pauseGameActive.png"));
            switch (heroType)
            {
                case "Knight" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/knightAva.png"));
                    break;
                case "Samurai" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/SamuraiAva.png"));
                    break;
                case "Archer" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/archerAva.png"));
                    break;
                case "Wizard" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/wizardAva.png"));
                    break;
                default:
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/knightAva.png"));
                    break;
            }
            frameAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/Khung ava.png"));
            frameBlood = new Texture(Gdx.files.internal("Menu/IngameIcon/Khung Blood(1)(1)-1.png.png"));
            bloodTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/Blood.png"));

            setupBloodFrames();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        createPauseButton();

        Gdx.input.setInputProcessor(stage);

    }

    private void setupBloodFrames()
    {
        fullBloodFrame = new TextureRegion();
        float frameWidth = bloodTexture.getWidth() / BLOOD_FRAME_COLS;
        float frameHeight = bloodTexture.getHeight() / BLOOD_FRAME_ROWS;

            fullBloodFrame = new TextureRegion(bloodTexture, 0 * (int)frameWidth, 0, (int)frameWidth, (int)frameHeight);
    }

    private void updateHealth(float health)
    {
        currentHealth = Math.max(0, Math.min(health, maxHealth));

//        System.out.println(currentHealth + "/" + maxHealth + ": " + healthPercent);

    }

    public void createPauseButton() {
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseUnactiveTexture);
        TextureRegionDrawable pauseActiveDrawable = new TextureRegionDrawable(pauseActiveTexture);

        pauseButton = new ImageButton(pauseDrawable, pauseActiveDrawable) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setPosition(Gdx.graphics.getWidth() - getWidth() - padding,
                    Gdx.graphics.getHeight() - getHeight() - padding);
            }
        };


        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Hien thi menu
                System.out.println("Pause button clicked");
                togglePause();
            }
        });

        stage.addActor(pauseButton);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        updateHealth(chosenHero.getCurrentHealth());
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();

        if(!isPaused)
        {


            batch.draw(frameAvatar,
                padding,
                topY,
                avatarFrameSize,
                avatarFrameSize);

            batch.draw(heroAvatar,
                padding + avatarPadding,
                topY + avatarPadding,
                avatarSize,
                avatarSize);


            batch.draw(frameBlood,
                bloodBarX,
                bloodBarY,
                bloodBarWidth,
                bloodBarHeight);


            //Cập nhật lại % máu
            float healthPercent = currentHealth /(float) maxHealth;
            float currentBloodWidth = innerBloodWidth * healthPercent;

            batch.draw(fullBloodFrame,
                bloodBarX + bloodBarInnerPaddingX ,
                bloodBarY + bloodBarInnerPaddingY  ,
                currentBloodWidth,
                innerBloodHeight );
        }

        batch.end();
        stage.draw();

        if (isPaused) {
            pauseScreen.render(Gdx.graphics.getDeltaTime());
        }
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
    public void hide() {

    }

    public void dispose() {
        stage.dispose();
        batch.dispose();
        pauseUnactiveTexture.dispose();
        pauseActiveTexture.dispose();
        heroAvatar.dispose();
        frameAvatar.dispose();
        frameBlood.dispose();
        bloodTexture.dispose();
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            pauseScreen.show();
        } else {
            pauseScreen.hide();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }
}
