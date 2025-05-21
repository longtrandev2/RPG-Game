package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.MeleeAttackComponent;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;
import com.myteam.rpgsurvivor.skills.SamuraiSkill;

public class Samurai extends Player {

    private InputHandle inputHandler;
    private HeroMovement heroMovement;
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

    private static final int RUN_FRAME_COLS = 16;
    private static final int RUN_FRAME_ROWS = 1;

    private static final int ATTACK_FRAME_COLS = 7;
    private static final int ATTACK_FRAME_ROWS = 1;

    private static final int SKILL_FRAME_COLS = 5;
    private static final int SKILL_FRAME_ROWS = 2;

    private static final int SMOKE_FRAME_COLS = 9;
    private static final int SMOKE_FRAME_ROWS = 1;

    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;
    public Samurai(float x, float y) {
        super(x, y, HeroType.SAMURAI);
        this.animationManager = new AnimationManager();
        this.heroMovement = new HeroMovement(this);
        this.inputHandler = new InputHandle(this, heroMovement);
        this.skillHandler = new SamuraiSkill(this);

        setupAnimations();
//        setupHitBox();
    }

    private void setupAnimations() {
        float idleFrameDuration = 0.15f;
        float runFrameDuration = 0.1f;
        float attackFrameDuration = this.getAttackSpeed();
        float skillFrameDuration = 0.08f;
        float smokeFrameDuration = 0.1f;
        float hurtFrameDuration = 0.1f;

        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/IDLE.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS, idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/RUN.png",
            RUN_FRAME_COLS, RUN_FRAME_ROWS, runFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/ATTACK 1.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS, attackFrameDuration,
            false
        );

        animationManager.addAnimation(
            StateType.STATE_SKILL.stateType,
            "Skills/Samurai Dash/Slash 1/color4/sprite-sheet.png",
            SKILL_FRAME_COLS, SKILL_FRAME_ROWS, skillFrameDuration,
            false
        );

        animationManager.addAnimation(
            StateType.STATE_SKILL_EFFECT.stateType,
            "Skills/Smoke/SmokeNDust P03 VFX 1.png",
            SMOKE_FRAME_COLS, SMOKE_FRAME_ROWS, smokeFrameDuration,
            false
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/HURT.png",
            HURT_FRAME_COLS, HURT_FRAME_ROWS, hurtFrameDuration,
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
            animationManager.setState(StateType.STATE_SKILL_EFFECT.stateType, false);

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
            animationManager.setState(StateType.STATE_ATTACK.stateType, false);
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
    public void update(float deltaTime) {
        deltaTime = 1/60f;
        updateWithDelta(deltaTime);
        super.update(deltaTime);
    }

    public void updateWithDelta(float deltaTime) {
        inputHandler.handleInput();

        skillHandler.update(deltaTime);

        if(isHurt)
        {
            hurtTimer -= deltaTime;
            if(hurtTimer <= 0)
            {
                isHurt = false;
            }
        }



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
            && !isAttacking && !skillHandler.isDashing() && animationManager.getCurrentState().equals("idle")) {
            isAttacking = true;
            attackTriggered = false;
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }

        if (!skillHandler.isDashing() && !animationManager.getCurrentState().equals("attack")) {
            heroMovement.update();
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
        if(isHurt)
        {
            animationManager.setState(StateType.STATE_HURT.stateType, true);
            return;
        }

        if (isAttacking) {
            float progress = animationManager.getAnimationProgress();
            System.out.println(progress);
            if (!attackTriggered && progress >= 0.85f) {
                attackHandler.tryAttack();
                attackTriggered = true;
            }
        }

        if (skillHandler.isDashing() || isUsingSkill) {
            if (!animationManager.getCurrentState().equals(StateType.STATE_SKILL.stateType)) {
                animationManager.setState(StateType.STATE_SKILL.stateType, true);
            }

            if (animationManager.isAnimationFinished()) {
                isUsingSkill = false;
                if (heroMovement.isMoving() && !skillHandler.isDashing()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else if (!skillHandler.isDashing()) {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
        }
        else if (isAttacking) {
            if (!animationManager.getCurrentState().equals(StateType.STATE_ATTACK.stateType)) {
                animationManager.setState(StateType.STATE_ATTACK.stateType, true);
            }

            if (animationManager.isAnimationFinished()) {
                isAttacking = false;
                if (heroMovement.isMoving()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
        }
        else if (heroMovement.isMoving() && !skillHandler.isDashing()) {
            animationManager.setState(StateType.STATE_RUN.stateType, true);
        }
        else if (!skillHandler.isDashing()) {
            animationManager.setState(StateType.STATE_IDLE.stateType, true);
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


    public void onHurt()
    {
        isHurt = true;
        hurtTimer = 0.4f;
        animationManager.setState(StateType.STATE_HURT.stateType, true);

    }

}

