package com.myteam.rpgsurvivor.model.impl.Creep;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.model.Entity;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

public class SummonedKnight extends Entity {
    private AnimationManager animationManager;
    private float remainingLife;
    private float stateTime = 0;
    private boolean facingRight = true;
    private boolean showSmoke;
    private float smokeStateTime = 0f;

    public static final String STATE_IDLE = "idle";
    public static final String STATE_WALK = "walk";
    public static final String STATE_ATTACK = "attack";
    public static final String STATE_SMOKE = "smoke";

    private static final int IDLE_FRAME_COLS = 6;
    private static final int IDLE_FRAME_ROWS = 1;
    private static final int WALK_FRAME_COLS = 8;
    private static final int WALK_FRAME_ROWS = 1;
    private static final int ATTACK_FRAME_COLS = 6;
    private static final int ATTACK_FRAME_ROWS = 1;
    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;
    private static final int DEATH_FRAME_COLS = 4;
    private static final int DEATH_FRAME_ROWS = 1;


    public SummonedKnight(float x, float y, float lifetime) {
        this.entityX = x;
        this.entityY = y;
        this.stat = HeroType.SUMMON_KNIGHT.stat;
        remainingLife = 3f; // Thoi gian ton tai 15s
        showSmoke = true;

        this.animationManager = new AnimationManager();

        setupAnimations();
    }
    private void setupAnimations()
    {
        float idleFrameDuration = 0.15f;
        float walkFrameDuration = 0.1f;
        float attackFrameDuration = 0.08f;
        float hurtFrameDuration = 0.1f;
        float deathFrameDuration = 0.1f;


        animationManager.addAnimation(
            STATE_IDLE,
            "Skills/Knight Spawn/Soldier-Idle.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS, idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_WALK,
            "Skills/Knight Spawn/Soldier-Walk.png",
            WALK_FRAME_COLS, WALK_FRAME_ROWS, walkFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_ATTACK,
             "Skills/Knight Spawn/Soldier-Attack01.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS, attackFrameDuration,
            false
        );

        animationManager.addAnimation(
            STATE_ATTACK,
            "Skills/Knight Spawn/Soldier-Hurt.png",
            HURT_FRAME_COLS, HURT_FRAME_ROWS, hurtFrameDuration,
            false
        );

        animationManager.addAnimation(
            STATE_ATTACK,
            "Skills/Knight Spawn/Soldier-Death.png",
            DEATH_FRAME_COLS, DEATH_FRAME_ROWS, deathFrameDuration,
            false
        );





        animationManager.setState(STATE_IDLE, true);
    }
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, entityX, entityY);
        }


    }


    public void update() {
        update(1/60f);
    }

    public void update(float deltaTime) {
        remainingLife -= deltaTime;
        animationManager.update(deltaTime);
        stateTime += deltaTime;

        if (stateTime > 2f) {
            if (Math.random() > 0.7) {
                if (animationManager.getCurrentState().equals(STATE_IDLE)) {
                    animationManager.setState(STATE_WALK, true);
                } else if (animationManager.getCurrentState().equals(STATE_WALK)) {
                    animationManager.setState(STATE_IDLE, true);
                }
            }

            if (Math.random() > 0.9) {
                animationManager.setState(STATE_ATTACK, true);
            }

            stateTime = 0;
        }

        if (animationManager.getCurrentState().equals(STATE_ATTACK) && animationManager.isAnimationFinished()) {
            animationManager.setState(STATE_IDLE, true);
        }
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
        animationManager.setFacingRight(facingRight);
    }

    public boolean isLifeTimeOver()
    {
        return remainingLife <= 0;
    }
}
