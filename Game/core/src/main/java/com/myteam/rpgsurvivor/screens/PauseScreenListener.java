package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.myteam.rpgsurvivor.Main;

public interface PauseScreenListener {
    void onBackButtonClicked();
    void onResumeButtonClicked();
    void onRestartButtonClicked();

}
