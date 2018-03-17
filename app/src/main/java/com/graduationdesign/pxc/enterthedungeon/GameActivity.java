package com.graduationdesign.pxc.enterthedungeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.graduationdesign.pxc.enterthedungeon.util.OnContinueClickListener;
import com.graduationdesign.pxc.enterthedungeon.view.GameLayout;

public class GameActivity extends AppCompatActivity {
    private GameLayout mGameLayout;
    private View left;
    private View right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        mGameLayout = (GameLayout) findViewById(R.id.game);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        left.setOnTouchListener(new OnContinueClickListener() {
            @Override
            public void handleClickEvent(View view) {
                mGameLayout.moveLeft();
            }
        });


        right.setOnTouchListener(new OnContinueClickListener() {
            @Override
            public void handleClickEvent(View view) {
                mGameLayout.moveRight();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameLayout.stop();
    }

}
