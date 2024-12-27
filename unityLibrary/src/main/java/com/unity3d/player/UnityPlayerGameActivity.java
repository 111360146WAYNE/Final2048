package com.unity3d.player;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;

import com.google.androidgamesdk.GameActivity;

public class UnityPlayerGameActivity extends GameActivity implements IUnityPlayerLifecycleEvents, IUnityPermissionRequestSupport, IUnityPlayerSupport {

    protected UnityPlayerForGameActivity mUnityPlayer;

    // 用來更新 Unity 的命令行參數
    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    static {
        System.loadLibrary("game");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 檢查 Intent 是否包含播放動畫的標記
        Intent intent = getIntent();
        boolean shouldPlayAnimation = intent.getBooleanExtra("playAnimation", false);

        if (shouldPlayAnimation) {
            // 播放動畫
            playAnimation();
        }

        // 確保 UI 和其他初始化代碼
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(true);
        }
    }

    @Override
    protected void onCreateSurfaceView() {
        super.onCreateSurfaceView();
        FrameLayout frameLayout = findViewById(contentViewId);

        applyInsetListener(mSurfaceView);

        mSurfaceView.setId(UnityPlayerForGameActivity.getUnityViewIdentifier(this));

        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);

        mUnityPlayer = new UnityPlayerForGameActivity(this, frameLayout, mSurfaceView, this);
    }

    private void applyInsetListener(SurfaceView surfaceView) {
        surfaceView.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> onApplyWindowInsets(surfaceView, ViewCompat.getRootWindowInsets(getWindow().getDecorView())));
    }

    private void playAnimation() {
        // 確保在主線程上執行 Unity 命令
        runOnUiThread(() -> {
            try {
                // 使用 Unity API 發送消息，通知 Unity 播放動畫
                UnityPlayer.UnitySendMessage("character_0", "PlayAnimation", "parameter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 添加動畫完成後的處理邏輯
        new Thread(() -> {
            try {
                // 假設動畫時間持續 30 秒（根據你的動畫實際時長調整）
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 動畫完成後返回主界面
            runOnUiThread(() -> {
                finish(); // 關閉當前的 UnityPlayerGameActivity
            });
        }).start();
    }

    @Override
    public UnityPlayerForGameActivity getUnityPlayerConnection() {
        return mUnityPlayer;
    }

    @Override
    public void onUnityPlayerUnloaded() {
        // 實現介面方法
    }

    @Override
    public void onUnityPlayerQuitted() {
        // 實現缺少的介面方法
        finish();
    }

    @Override
    protected void onDestroy() {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mUnityPlayer.onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mUnityPlayer.onStart();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mUnityPlayer.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mUnityPlayer.onResume();
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mUnityPlayer.configurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mUnityPlayer.windowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(PermissionRequest request) {
        mUnityPlayer.addPermissionRequest(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mUnityPlayer.permissionResponse(this, requestCode, permissions, grantResults);
    }
}