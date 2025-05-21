package com.myteam.rpgsurvivor.model.impl.Hero;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;
import com.myteam.rpgsurvivor.skills.KnightSkill;

public class Knight extends Player{
    private KnightSkill knightSkill;
    private InputHandle inputHandle;
    private HeroMovement heroMovement;
    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;

    private static final int IDLE_FRAME_COLS = 7;
    private static final int IDLE_FRAME_ROWS = 1;

    private static final int RUN_FRAME_COLS = 8;
    private static final int RUN_FRAME_ROWS = 1;

    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;

    private static final int SKILL_FRAME_COLS = 6;
    private static final int SKILL_FRAME_ROWS = 1;

    private static final int ATTACK_FRAME_COLS = 6;
    private static final int ATTACK_FRAME_ROWS = 1;


    public Knight(float x, float y)
    {
        super(x,y,HeroType.KNIGHT);
        this.animationManager = new AnimationManager();
        this.heroMovement = new HeroMovement(this);
        this.inputHandle = new InputHandle(this, heroMovement);
        this.knightSkill = new KnightSkill(this);
        setupAnimation();
//        setupHitBox();
    }

    private void setupAnimation()
    {
        float idleFrameDuration = 0.15f;
        float runFrameDuration = 0.1f;
        float attackFrameDuration = this.getAttackSpeed();
        float skillFrameDuration = 0.08f;
        float hurtFrameDuration = 0.1f;


        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/IDLE.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS,idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/RUN.png",
            RUN_FRAME_COLS, RUN_FRAME_ROWS,runFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/ATTACK 1.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS,attackFrameDuration,
            false
        );

        animationManager.addAnimation(
            StateType.STATE_SKILL.stateType,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/DEFEND.png",
            SKILL_FRAME_COLS, SKILL_FRAME_ROWS,skillFrameDuration,
            false
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/HURT.png",
            HURT_FRAME_COLS, HURT_FRAME_ROWS, hurtFrameDuration,
            false
        );
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {

        stateTime += deltaTime;
        animationManager.update(deltaTime);

        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if(currentFrame != null)
        {
            batch.draw(
                currentFrame,
                entityX, entityY
            );
        }

        knightSkill.render(batch, deltaTime);
    }

    @Override
    public void update(float deltaTime) {
        deltaTime = 1/60f;
        updateWithDeltaTime(deltaTime);
        super.update(deltaTime);
    }

    public void updateWithDeltaTime(float deltaTime)
    {
        knightSkill.update(deltaTime);
        if(isHurt)
        {
            hurtTimer -= deltaTime;
            if(hurtTimer <= 0)
            {
                isHurt = false;
            }
        }
        if (!isAttacking) {
            inputHandle.handleInput();

            if (inputHandle.isActionActive(InputHandle.ACTION_ATTACK) && attackHandler.canAttack()
                && !isAttacking  && animationManager.getCurrentState().equals("idle")) {
                isAttacking = true;
                attackTriggered = false;
                animationManager.setState(StateType.STATE_ATTACK.stateType, true);
            }

            if (inputHandle.isActionActive(InputHandle.ACTION_SKILL)) {
                if(knightSkill.activateSkill())
                {
                    isUsingSkill = true;
                    animationManager.setState(StateType.STATE_SKILL.stateType, true);
                }
            }

            if(!animationManager.getCurrentState().equals("attack"))
                heroMovement.update();

            if (heroMovement.isMoving()) {
                knightSkill.updatePositions();
            }
        } else {
            inputHandle.handleInput();
        }
        updateAnimationState(deltaTime);
    }

    public void updateAnimationState(float deltaTime)
    {
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

        if (isUsingSkill) {
            if (animationManager.isAnimationFinished()) {
                isUsingSkill = false;
                if (heroMovement.isMoving()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
            return;
        }

        if (isAttacking) {
            if (animationManager.isAnimationFinished()) {
                isAttacking = false;
                if (heroMovement.isMoving()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
            return;
        }


        if (heroMovement.isMoving()) {
            animationManager.setState(StateType.STATE_RUN.stateType, true);
        } else {
            animationManager.setState(StateType.STATE_IDLE.stateType, true);
        }

        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);
    }

    @Override
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    @Override
    public void onHurt()
    {
        isHurt = true;
        hurtTimer = 0.4f;
        animationManager.setState(StateType.STATE_HURT.stateType, true);
    }
}
