package com.myteam.rpgsurvivor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.model.Player;

public class InputHandle {
    private Player player;
    private HeroMovement heroMovement;

    public static final int ACTION_MOVE_UP = 0;
    public static final int ACTION_MOVE_DOWN = 1;
    public static final int ACTION_MOVE_LEFT = 2;
    public static final int ACTION_MOVE_RIGHT = 3;
    public static final int ACTION_ATTACK = 4;
    public static final int ACTION_SKILL = 5;

    private boolean[] actions;

    public InputHandle(Player player , HeroMovement heroMovement)
    {
        this.player = player;
        this.heroMovement = heroMovement;
        this.actions = new boolean[10];
    }

    public boolean handleInput()
    {
        resetActions();

        // Thay đổi từ isKeyJustPressed sang isKeyPressed để kiểm tra khi phím đang được giữ
        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            actions[ACTION_MOVE_UP] = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            actions[ACTION_MOVE_DOWN] = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            actions[ACTION_MOVE_LEFT] = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            actions[ACTION_MOVE_RIGHT] = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.J))
        {
            actions[ACTION_ATTACK] = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.K))
        {
            actions[ACTION_SKILL] = true;
        }

        // Nếu đang di chuyển, cập nhật hướng di chuyển
        if (isMoving()) {
            heroMovement.setMovementDirection(
                actions[ACTION_MOVE_UP],
                actions[ACTION_MOVE_DOWN],
                actions[ACTION_MOVE_LEFT],
                actions[ACTION_MOVE_RIGHT]
            );
        } else {
            // Thêm xử lý khi không có phím di chuyển nào được nhấn - về trạng thái Idle
            heroMovement.setMovementDirection(false, false, false, false);
        }


        return isAnyActionActive();
    }

    /**
     * Đặt lại trạng thái tất cả các hành động
     */
    private void resetActions() {
        for (int i = 0; i < actions.length; i++) {
            actions[i] = false;
        }
    }

    /**
     * Kiểm tra xem có bất kỳ hành động nào đang được thực hiện không
     * @return true nếu có bất kỳ hành động nào được kích hoạt
     */
    public boolean isAnyActionActive() {
        for (boolean action : actions) {
            if (action) return true;
        }
        return false;
    }

    /**
     * Kiểm tra xem người chơi có đang di chuyển không
     * @return true nếu người chơi đang di chuyển
     */
    public boolean isMoving() {
        return actions[ACTION_MOVE_UP] || actions[ACTION_MOVE_DOWN] ||
            actions[ACTION_MOVE_LEFT] || actions[ACTION_MOVE_RIGHT];
    }

    public boolean isAttack()
    {
        return actions[ACTION_ATTACK];
    }
    /**
     * Kiểm tra một hành động cụ thể
     * @param actionCode Mã hành động cần kiểm tra
     * @return true nếu hành động đang được kích hoạt
     */
    public boolean isActionActive(int actionCode) {
        if (actionCode >= 0 && actionCode < actions.length) {
            return actions[actionCode];
        }
        return false;
    }

    /**
     * Đặt trạng thái của một hành động
     * @param actionCode Mã hành động cần đặt
     * @param active Trạng thái kích hoạt
     */
    public void setAction(int actionCode, boolean active) {
        if (actionCode >= 0 && actionCode < actions.length) {
            actions[actionCode] = active;
        }
    }
}
