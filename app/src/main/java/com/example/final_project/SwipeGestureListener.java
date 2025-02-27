package com.example.final_project;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GameBoard gameBoard;

    public SwipeGestureListener(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            // 水平滑動
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    // 向右滑動
                    gameBoard.moveRight();
                } else {
                    // 向左滑動
                    gameBoard.moveLeft();
                }
                result = true;
            }
        } else {
            // 垂直滑動
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    // 向下滑動
                    gameBoard.moveDown();
                } else {
                    // 向上滑動
                    gameBoard.moveUp();
                }
                result = true;
            }
        }

        return result;
    }
}
