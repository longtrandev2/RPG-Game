package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.enum_type.BossType;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public abstract class Enemy extends Entity {
    // Stat
    protected int level;
    protected float attackCooldown;
    protected float attackTimer;
    protected Player targetPlayer;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;


    protected float detectionRange;
    protected float attackRange;

    private float offsetX;
    private float offsetY;

    private StateType currentState;
    private EnemyMovement enemyMovement;
    private ShapeRenderer shapeRenderer;
    private Rectangle hitboxPlayer;
    private Rectangle hitboxEnemy;


    private Vector2 velocity = new Vector2();
    private float knockbackDecay = 10f;
    private float maxKnockbackSpeed = 300f;

    private boolean bossTurn;
    private Texture frameHP;
    private Texture bloodTexture;

    private TextureRegion fullBloodFrame;

    private static final int BLOOD_FRAME_COLS = 6;
    private static final int BLOOD_FRAME_ROWS = 1;


    private float bloodBarWidth = 200;
    private float bloodBarHeight = 40;
    private float padding = 10;
    private float bloodBarInnerPaddingX = 10;
    private float bloodBarInnerPaddingY = 10;

    private float innerBloodWidth;
    private float innerBloodHeight;
    private float bloodBarX;
    private float bloodBarY;

    public Enemy(float x, float y, MonsterType enemyType, Player player, AnimationForEnemy animationFactory) {
        this.entityX = x;
        this.entityY = y;

        // Stat
        this.stat = enemyType.stat;
        this.level = 1;
        this.currentHealth = stat.maxHealth;

        // Khởi tạo hitbox
        this.hitbox = enemyType.hitbox.createHitbox(entityX,entityY);
        offsetX = enemyType.hitbox.getOffsetX();
        offsetY = enemyType.hitbox.getOffsetY();


        // Thiết lập player là mục tiêu
        this.targetPlayer = player;

        entityX = x;
        entityY = y;
        hitboxPlayer = targetPlayer.getHitbox();
        hitboxEnemy = getHitBox();
        this.enemyMovement = new EnemyMovement(x,y,player,enemyType.stat.moveSpeed);
        this.animationManager = animationFactory.createEnemyAnimation(enemyType);
        this.currentState = StateType.STATE_IDLE;
        shapeRenderer = new ShapeRenderer();


        this.attackCooldown = 1f / stat.attackSpeed;
        this.attackTimer = 0;
        this.attackRange = stat.rangeAttack;
        this.detectionRange = 1000f;

        this.movement = new EnemyMovement(x,y, player, stat.moveSpeed);

        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;
        this.isAttack = false;

        this.bossTurn = false;

        this.attackbox = new Rectangle(hitbox);
        attackbox.setSize(hitbox.getWidth() + attackRange , hitbox.getHeight() + attackRange);
    }

    public Enemy(float x, float y, BossType bossType, Player player, AnimationForEnemy animationFactory) {
        this.entityX = x;
        this.entityY = y;

        // Stat
        this.stat = bossType.stat;
        this.level = 1;
        this.currentHealth = stat.maxHealth;

        // Khởi tạo hitbox
        this.hitbox = bossType.hitbox.createHitbox(entityX,entityY);
        offsetX = bossType.hitbox.getOffsetX();
        offsetY = bossType.hitbox.getOffsetY();


        // Thiết lập player là mục tiêu
        this.targetPlayer = player;

        entityX = x;
        entityY = y;
        hitboxPlayer = targetPlayer.getHitbox();
        hitboxEnemy = getHitBox();
        this.enemyMovement = new EnemyMovement(x,y,player,bossType.stat.moveSpeed);
        this.animationManager = animationFactory.createBossAnimation(bossType);
        this.currentState = StateType.STATE_IDLE;
        shapeRenderer = new ShapeRenderer();


        this.attackCooldown = 1f / stat.attackSpeed;
        this.attackTimer = 0;
        this.attackRange = stat.rangeAttack;
        this.detectionRange = 1000f;

        this.movement = new EnemyMovement(x,y, player, stat.moveSpeed);

        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;
        this.isAttack = false;

        this.bossTurn = true;

        // Khởi tạo textures cho thanh máu của boss
        frameHP = new Texture(Gdx.files.internal("Enemy/Asset For Boss/FrameHP.png"));
        bloodTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/Blood.png"));

        // Khởi tạo kích thước thanh máu
        innerBloodWidth = bloodBarWidth - (2 * bloodBarInnerPaddingX) + 15;
        innerBloodHeight = bloodBarHeight - (2 * bloodBarInnerPaddingY);
        bloodBarX = padding + padding;
        bloodBarY = bloodBarHeight / 2;

        // Thiết lập frame cho máu
        setupBloodFrames();

        this.attackbox = new Rectangle(hitbox);
        attackbox.setSize(hitbox.getWidth() + attackRange , hitbox.getHeight() + attackRange);
    }

    private void setupBloodFrames() {
        fullBloodFrame = new TextureRegion();
        float frameWidth = bloodTexture.getWidth() / BLOOD_FRAME_COLS;
        float frameHeight = bloodTexture.getHeight() / BLOOD_FRAME_ROWS;

        fullBloodFrame = new TextureRegion(bloodTexture, 0 * (int)frameWidth, 0, (int)frameWidth, (int)frameHeight);
    }

    private void updateHealth(float health) {
        currentHealth = (int) Math.max(0, Math.min(health, getMaxHealth()));
    }

    @Override
    public void update(float deltaTime) {
        updateHealth(getCurrentHealth());
//        System.out.println(getCurrentHealth());
        // Cập nhật vị trí hitbox
        if (!velocity.isZero()) {
            entityX += velocity.x * deltaTime;
            entityY += velocity.y * deltaTime;

            float decay = knockbackDecay * deltaTime;
            if (velocity.len() <= decay) {
                velocity.setZero();
            } else {
                velocity.scl(1 - decay / velocity.len());
            }
        }

        hitbox.setPosition(entityX + offsetX, entityY + offsetY);

        // Cập nhật vị trí attackbox
        if(isFacingRight()){
            attackbox.setPosition(entityX + offsetX , entityY + offsetY);
        } else {
            attackbox.setPosition(entityX + offsetX - attackRange, entityY + offsetY);
        }

        if (isDead) return;

        if (isHurt) {
            hurtTimer -= deltaTime;
            if (hurtTimer <= 0) {
                isHurt = false;
            } else {
                currentState = StateType.STATE_HURT;
                animationManager.setState(StateType.STATE_HURT.stateType, true);
                return;
            }
        }

        // Kiểm tra player có trong tầm phát hiện không
        if (isPlayerInDetectionRange()) {
            if (isPlayerInAttackRange()) {
                tryAttack();
            }
        }

        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }

        float dx = 0;
        // Tính hiệu dx nhỏ nhất
        if(hitboxEnemy.getX() + hitboxEnemy.getWidth()/2 < hitboxPlayer.getX())
            dx = hitboxPlayer.getX() - (hitboxEnemy.getX() + hitboxEnemy.getWidth()/2);
        else if(hitboxPlayer.getX() + hitboxPlayer.getWidth() < hitboxEnemy.getX())
            dx = hitboxEnemy.getX() - (hitboxPlayer.getX() + hitboxPlayer.getWidth()/2);

        float dy = 0;
        // Tính hiệu dy nhỏ nhất
        if(hitboxEnemy.getY() + hitboxEnemy.getHeight()/2 < hitboxPlayer.getY())
            dy = hitboxPlayer.getY() - (hitboxEnemy.getY() + hitboxEnemy.getHeight()/2);
        else if(hitboxPlayer.getY() + hitboxPlayer.getHeight()/2 < hitboxEnemy.getY())
            dy = hitboxEnemy.getY() - (hitboxPlayer.getY() + hitboxPlayer.getHeight()/2);

        float shortestDistanceToPlayer = (float) Math.sqrt(dx * dx + dy * dy);

        if (shortestDistanceToPlayer <= detectionRange && shortestDistanceToPlayer > attackRange) {
            currentState = StateType.STATE_RUN;
            // Sử dụng deltaTime được truyền vào
            Vector2 newDirection = enemyMovement.move(deltaTime);
            entityX = newDirection.x;
            entityY = newDirection.y;
        }
        else if (attackbox.overlaps(hitboxPlayer)) {
            if (isAttack) {
                currentState = StateType.STATE_ATTACK;
            } else {
                currentState = StateType.STATE_IDLE;
            }
        } else {
            currentState = StateType.STATE_IDLE;
        }

        if (targetPlayer.getHitbox().x > entityX) {
            facingRight = true;
        } else {
            facingRight = false;
        }

        if (animationManager != null) {
            animationManager.setState(currentState.stateType, true);

            if (isAttack && animationManager.isAnimationFinished()) {
                isAttack = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        if (isDead || animationManager == null) return;

        update(deltaTime);

        animationManager.update(deltaTime);

        // Thiết lập hướng di chuyển
        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);


        // Vẽ nhân vật
        batch.draw(animationManager.getCurrentFrame(), entityX, entityY);

        // Vẽ thanh máu nếu là boss
        if (bossTurn) {
            // Vẽ khung thanh máu
            batch.draw(frameHP, bloodBarX, bloodBarY, bloodBarWidth, bloodBarHeight);

            // Tính toán tỷ lệ máu hiện tại và vẽ thanh máu
            float healthPercent = currentHealth / (float) getMaxHealth();
            float currentBloodWidth = innerBloodWidth * healthPercent;

            batch.draw(fullBloodFrame,
                bloodBarX + bloodBarInnerPaddingX,
                bloodBarY + bloodBarInnerPaddingY,
                currentBloodWidth,
                innerBloodHeight);
        }

        // Debug drawing
        DebugRenderer.drawRect(hitboxPlayer, Color.GREEN);
        DebugRenderer.drawRect(getHitBox(), Color.YELLOW);
        DebugRenderer.drawRect(attackbox, Color.RED);

    }

    public boolean isPlayerInDetectionRange() {
        float distance = Vector2.dst(entityX, entityY, targetPlayer.getHitbox().getX(), targetPlayer.getHitbox().getY());
        return distance <= detectionRange;
    }

    public boolean isPlayerInAttackRange() {
        return attackbox.overlaps(targetPlayer.hitbox);
    }

    private void tryAttack() {
        if (attackTimer <= 0 && !isAttack) {
            performAttack();
            attackTimer = attackCooldown;
        }
    }

    protected void performAttack() {
        isAttack = true;
        if (animationManager != null) {
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }

        if(currentState == StateType.STATE_ATTACK) {
            targetPlayer.takeDamge(getDamage());
            System.out.println("takedamge");
            targetPlayer.onHurt();
        }
    }

    public void setDetectionRange(float range) {
        this.detectionRange = range;
    }

    public void setAttackRange(float range) {
        this.attackRange = range;
    }

    public Rectangle getHitBox() {
        return hitbox;
    }

    public boolean isDead() {
        return isDead;
    }

    public void onHurt() {
        isHurt = true;
        hurtTimer = 0.4f;
        animationManager.setState(StateType.STATE_HURT.stateType, true);
    }

    public void applyKnockback(Vector2 force) {
        // Giới hạn lực bật lùi không vượt quá max
        if (force.len() > maxKnockbackSpeed) {
            force.setLength(maxKnockbackSpeed);
        }
        velocity.add(force);
    }
}
