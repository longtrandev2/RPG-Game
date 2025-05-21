package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface UpgradeScreenListener {
    void onHealthInClicked();
    void onDamageInClicked();
    void onSpeedInClicked();
    void onAtkSpeedInClicked();
    void onSkillInClicked();

    void onHealthDeClicked();
    void onDamageDeClicked();
    void onSpeedDeClicked();
    void onAtkSpeedDeClicked();
    void onSkillDeClicked();
}
