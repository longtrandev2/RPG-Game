package com.myteam.rpgsurvivor.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

import java.util.Arrays;
import java.util.HashMap;

public class AnimationManager {

    public HashMap<String, Animation<TextureRegion>> animations;

    private String currentState;

    private float stateTime;

    private boolean facingRight;

    public AnimationManager() {
        this.animations = new HashMap<>();
        this.currentState = StateType.STATE_IDLE.stateType;
        this.stateTime = 0f;
        this.facingRight = true;
    }

    public void addAnimation(String stateName, String pathAnimation, int cols, int rows, float frameDuration, boolean loop) {
        Animation<TextureRegion> animation = AnimationLoader.getInstance().loadAnimation(pathAnimation, cols, rows, frameDuration);
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        animations.put(stateName, animation);

        if (animations.size() == 1) {
            currentState = stateName;
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    public void setState(String newState, boolean resetStateTime) {
        if (!animations.containsKey(newState)) {
            System.out.println("Warning: Animation state '" + newState + "' not found");
            return;
        }

        if (!currentState.equals(newState)) {
            currentState = newState;
            if (resetStateTime) {
                stateTime = 0;
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        Animation<TextureRegion> currentAnimation = animations.get(currentState);
//        System.out.println(currentState);
        if (currentAnimation == null) {
            if (!animations.isEmpty()) {
                currentAnimation = animations.values().iterator().next();
            } else {
                return null;
            }
        }

        // Kiểm tra xem animation hiện tại có phải là loại lặp hay không
        boolean looping = currentAnimation.getPlayMode() == Animation.PlayMode.LOOP ||
            currentAnimation.getPlayMode() == Animation.PlayMode.LOOP_PINGPONG;

        // Sử dụng tham số looping dựa trên chế độ chơi của animation
        TextureRegion frame = currentAnimation.getKeyFrame(stateTime, looping);

        // Xử lý lật hướng nhân vật
        if (frame.isFlipX() != !facingRight) {
            frame.flip(true, false);
        }

        return frame;
    }


    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isAnimationFinished() {
        Animation<TextureRegion> currentAnimation = animations.get(currentState);
        return currentAnimation != null && currentAnimation.isAnimationFinished(stateTime);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void resetStateTime() {
        stateTime = 0;
    }

    public boolean hasState(String state) {
        return animations.containsKey(state);
    }


    public float getAnimationProgress() {
        Animation<TextureRegion> currentAnimation = animations.get(currentState);
        if (currentAnimation == null) return 0f;

        float duration = currentAnimation.getAnimationDuration();
        if (duration == 0) return 1f;

        return Math.min(stateTime / duration, 1f);
    }

    public AnimationManager copy() {
        AnimationManager newManager = new AnimationManager();

        for (HashMap.Entry<String, Animation<TextureRegion>> entry : this.animations.entrySet()) {
            Animation<TextureRegion> original = entry.getValue();

            // Clone từng frame thay vì gọi getKeyFrames()
            float frameDuration = original.getFrameDuration();
            int frameCount = (int) (original.getAnimationDuration() / frameDuration);

            Array<TextureRegion> framesArray = new Array<>();

            for (int i = 0; i < frameCount; i++) {
                TextureRegion frame = original.getKeyFrame(i * frameDuration);
                framesArray.add(new TextureRegion(frame)); // clone frame
            }

            Animation<TextureRegion> copied = new Animation<>(frameDuration, framesArray);
            copied.setPlayMode(original.getPlayMode());

            newManager.animations.put(entry.getKey(), copied);
        }

        newManager.setState(this.currentState, true);
        return newManager;
    }


}
