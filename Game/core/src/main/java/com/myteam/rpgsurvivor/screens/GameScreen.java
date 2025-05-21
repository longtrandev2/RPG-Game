package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.myteam.rpgsurvivor.Main;

public class GameScreen implements Screen {
    private final Main game;
    private final MapScreen map;
    private final String heroType;
    private float gameTime = 0;

    public GameScreen (Main game, String heroType)
    {
        this.game = game;
        this.heroType = heroType;
        this.map = new MapScreen(heroType, game);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameTime += delta;
        map.update();

        map.render(delta);
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

    }
}
