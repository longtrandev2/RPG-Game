package com.myteam.rpgsurvivor.animation;

import com.myteam.rpgsurvivor.model.enum_type.BossType;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class AnimationForEnemy {
    private static final float DEFAULT_FRAME_DURATION = 0.1f;

    public AnimationManager createEnemyAnimation(MonsterType monsterType)
    {
        AnimationManager animationManager = new AnimationManager();

        switch (monsterType)
        {
            case GOBLIN:
                setUpGoblinAnimation(animationManager);
                break;
            case SKELETON:
                setUpSkeletonAnimation(animationManager);
                break;
            case RAT:
                setUpRatAnimation(animationManager);
                break;
            case ORC:
                setUpOrcAnimation(animationManager);
                break;
            case VAMPIRE:
                setUpVampireAnimation(animationManager);
                break;

        }
        return animationManager;
    }

    public AnimationManager createBossAnimation(BossType monsterType)
    {
        AnimationManager animationManager = new AnimationManager();

        switch (monsterType)
        {
            case SLIME_BOSS:
                setUpSlimeBossAnimation(animationManager);
                break;

        }
        return animationManager;
    }

    private void setUpGoblinAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_idle.png",
            4,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_attack.png",
            8,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_run.png",
            4,1, DEFAULT_FRAME_DURATION, true
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_hurt.png",
            1,1, DEFAULT_FRAME_DURATION, true
        );
    }

    public void setUpSkeletonAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Idle.png",
            11,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Walk.png",
            13,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Hit.png",
            8,1, DEFAULT_FRAME_DURATION,true
        );
        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Attack.png",
            18,1, DEFAULT_FRAME_DURATION,false
        );
    }

    public void setUpRatAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-idle.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-run.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-attack.png",
            6,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-hurt.png",
            1,1,DEFAULT_FRAME_DURATION,false
        );
    }

    public void setUpOrcAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Creep 3/Monster_Creatures_Fantasy(Version 1.3)/Orc/Orc-Idle.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Creep 3/Monster_Creatures_Fantasy(Version 1.3)/Orc/Orc-Walk.png",
            8,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Creep 3/Monster_Creatures_Fantasy(Version 1.3)/Orc/Orc-Attack01.png",
            6,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Enemy/Creep 3/Monster_Creatures_Fantasy(Version 1.3)/Orc/Orc-Hurt.png",
            4,1,DEFAULT_FRAME_DURATION,false
        );
    }

    public void setUpVampireAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Creep 2/Enemy_Animations_Set/enemies-vampire_idle.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Creep 2/Enemy_Animations_Set/enemies-vampire_movement.png",
            8,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Creep 2/Enemy_Animations_Set/enemies-vampire_attack.png",
            16,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Enemy/Creep 2/Enemy_Animations_Set/enemies-vampire_take_damage.png",
            5,1,DEFAULT_FRAME_DURATION,false
        );
    }

    public void setUpSlimeBossAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Slime Boss/boss_demon_slime_FREE_v1.0/single sprites/demon_idle_1(1).png",
            5,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Slime Boss/boss_demon_slime_FREE_v1.0/single sprites/demon_walk_12(1).png",
            12,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Slime Boss/boss_demon_slime_FREE_v1.0/single sprites/demon_cleave_1(1).png",
            15,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Enemy/Slime Boss/boss_demon_slime_FREE_v1.0/single sprites/demon_take_hit_1(1).png",
            5,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_DEATH.stateType,
            "Enemy/Slime Boss/boss_demon_slime_FREE_v1.0/single sprites/demon_death_1.png",
            22,1,DEFAULT_FRAME_DURATION, false
        );
    }


}
