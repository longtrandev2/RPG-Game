// AnimationLoader.java
package com.myteam.rpgsurvivor.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class AnimationLoader {

    private static AnimationLoader instance;

    private HashMap<String, Animation<TextureRegion>> animationCache;

    private AnimationLoader() {
        animationCache = new HashMap<>();
    }

    public static AnimationLoader getInstance() {
        if (instance == null) {
            instance = new AnimationLoader();
        }
        return instance;
    }


    public Animation<TextureRegion> loadAnimation(String path, int cols, int rows, float frameDuration) {
        String key = path + "_" + cols + "_" + rows + "_" + frameDuration;

        if (animationCache.containsKey(key)) {
            return animationCache.get(key);
        }

        Texture sheet = new Texture(Gdx.files.internal(path));

        TextureRegion[][] tmp = TextureRegion.split(
            sheet,
            sheet.getWidth() / cols,
            sheet.getHeight() / rows);

        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames.add(tmp[i][j]);
            }
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);

        animationCache.put(key, animation);

        return animation;
    }


    public void clearCache() {
        animationCache.clear();
    }
}
