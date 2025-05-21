package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum StateType {
    STATE_IDLE ("idle"),
    STATE_RUN("run"),
    STATE_ATTACK("attack"),
    STATE_SKILL("skills"),
    STATE_SKILL_EFFECT("skillEffects"),
    STATE_HURT("hurt"),
    STATE_DEATH("death");
    public final String stateType;

    StateType(String stateType) {
        this.stateType = stateType;
    }
}
