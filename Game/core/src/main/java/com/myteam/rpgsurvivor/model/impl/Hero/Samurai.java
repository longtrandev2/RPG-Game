package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.SamuraiAttack;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.skills.SamuraiSkill;

public class Samurai extends Player {

    private InputHandle inputHandler;
    private Movement movement;
    private SamuraiAttack attackHandler;
    private SamuraiSkill skillHandler;
    private boolean facingRight = true;
    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;

    private boolean showSmoke = false;
    private float smokeX, smokeY;
    private float smokeStateTime = 0f;
    private float skillEffectX, skillEffectY;
    private float skillEffectStateTime = 0f;
    private boolean showSkillEffect = false;

    private boolean showDashAttack = false;
    private float dashAttackStateTime = 0f;


    private static final int IDLE_FRAME_COLS = 10;
    private static final int IDLE_FRAME_ROWS = 1;
    private static final int WALK_FRAME_COLS = 16;
    private static final int WALK_FRAME_ROWS = 1;
    private static final int ATTACK_FRAME_COLS = 7;
    private static final int ATTACK_FRAME_ROWS = 1;
    private static final int SKILL_FRAME_COLS = 5;
    private static final int SKILL_FRAME_ROWS = 2;
    private static final int SMOKE_FRAME_COLS = 9;
    private static final int SMOKE_FRAME_ROWS = 1;

    public static final String STATE_IDLE = "idle";
    public static final String STATE_WALK = "walk";
    public static final String STATE_ATTACK = "attack";
    public static final String STATE_SKILL = "skills";
    public static final String STATE_SMOKE = "smoke";

    public Samurai(float x, float y, int health, int damage, float speed) {
        super(x, y, health, damage, speed);
        this.animationManager = new AnimationManager();
        this.movement = new Movement(this);
        this.inputHandler = new InputHandle(this, movement);
        this.attackHandler = new SamuraiAttack(this);
        this.skillHandler = new SamuraiSkill(this);

        setupAnimations();
    }

    private void setupAnimations() {
        float idleFrameDuration = 0.15f;
        float walkFrameDuration = 0.1f;
        float attackFrameDuration = 0.08f;
        float skillFrameDuration = 0.08f;
        float smokeFrameDuration = 0.1f;

        animationManager.addAnimation(
            STATE_IDLE,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/IDLE.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS, idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_WALK,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/RUN.png",
            WALK_FRAME_COLS, WALK_FRAME_ROWS, walkFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_ATTACK,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/ATTACK 1.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS, attackFrameDuration,
            false
        );

        animationManager.addAnimation(
            STATE_SKILL,
            "Skills/Samurai Dash/Slash 1/color4/sprite-sheet.png",
            SKILL_FRAME_COLS, SKILL_FRAME_ROWS, skillFrameDuration,
            false
        );

        animationManager.addAnimation(
            STATE_SMOKE,
            "Skills/Smoke/SmokeNDust P03 VFX 1.png",
            SMOKE_FRAME_COLS, SMOKE_FRAME_ROWS, smokeFrameDuration,
            false
        );
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        stateTime += deltaTime;
        animationManager.update(deltaTime);

        // Render nhân vật chính
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(
                currentFrame,
                entityX, entityY
            );
        }

        if(showSmoke)
        {
            smokeStateTime += deltaTime;
            String currentState = animationManager.getCurrentState();
            animationManager.setState(STATE_SMOKE, false);

            TextureRegion smokeFrame = animationManager.getCurrentFrame();
            if (smokeFrame != null) {
                batch.draw(smokeFrame, smokeX, smokeY);
            }

            animationManager.setState(currentState, false);

            // Kết thúc hiệu ứng khói nếu đã hoàn thành
            if (animationManager.isAnimationFinished()) {
                showSmoke = false;
                smokeStateTime = 0f;
            }
        }

        if (showDashAttack) {
            dashAttackStateTime += deltaTime;
            String currentState = animationManager.getCurrentState();
            animationManager.setState(STATE_ATTACK, false);
            TextureRegion attackFrame = animationManager.getCurrentFrame();

            if (attackFrame != null) {

                batch.draw(attackFrame, entityX, entityY);
            }

            animationManager.setState(currentState, false);

            if (animationManager.isAnimationFinished()) {
                showDashAttack = false;
                dashAttackStateTime = 0f;
            }
        }
    }

    @Override
    public void update() {
        float deltaTime = 1/60f;
        updateWithDelta(deltaTime);
    }

    public void updateWithDelta(float deltaTime) {
        inputHandler.handleInput();

        skillHandler.update(deltaTime);

        if (inputHandler.isActionActive(InputHandle.ACTION_SKILL) && !isAttacking && !isUsingSkill
            && skillHandler.canDash()) {
            isUsingSkill = true;

            showSmoke = true;
            smokeStateTime = 0f;
            smokeX = entityX - 10;
            smokeY = entityY - 10;

            showDashAttack = true;
            dashAttackStateTime = 0f;

            skillHandler.performDash(facingRight, inputHandler);
        }

        if (inputHandler.isActionActive(InputHandle.ACTION_ATTACK) && attackHandler.canAttack()
            && !isAttacking && !skillHandler.isDashing()) {
            isAttacking = true;
            attackHandler.executeAttack();
        }

        if (!skillHandler.isDashing()) {
            movement.update();
        } else {
            if (skillHandler.getDashProgress() > 0.8f && !showSkillEffect) {
                showSkillEffect = true;
                skillEffectStateTime = 0f;
                skillEffectX = entityX + (facingRight ? 30 : -30);
                skillEffectY = entityY;

                showSmoke = true;
                smokeStateTime = 0f;
                smokeX = entityX - 10;
                smokeY = entityY - 10;
            }
        }
        updateAnimationState(deltaTime);


    }

    private void updateAnimationState(float deltaTime) {
        if (skillHandler.isDashing() || isUsingSkill) {
            if (!animationManager.getCurrentState().equals(STATE_SKILL)) {
                animationManager.setState(STATE_SKILL, true);
            }

            if (animationManager.isAnimationFinished()) {
                isUsingSkill = false;
                if (movement.isMoving() && !skillHandler.isDashing()) {
                    animationManager.setState(STATE_WALK, true);
                } else if (!skillHandler.isDashing()) {
                    animationManager.setState(STATE_IDLE, true);
                }
            }
        }
        else if (isAttacking) {
            if (!animationManager.getCurrentState().equals(STATE_ATTACK)) {
                animationManager.setState(STATE_ATTACK, true);
            }

            if (animationManager.isAnimationFinished()) {
                isAttacking = false;
                if (movement.isMoving()) {
                    animationManager.setState(STATE_WALK, true);
                } else {
                    animationManager.setState(STATE_IDLE, true);
                }
            }
        }
        else if (movement.isMoving() && !skillHandler.isDashing()) {
            animationManager.setState(STATE_WALK, true);
        }
        else if (!skillHandler.isDashing()) {
            animationManager.setState(STATE_IDLE, true);
        }

        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);
    }

    @Override
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean getFacingRight() {
        return facingRight;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public void setAnimationManager(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }


}
