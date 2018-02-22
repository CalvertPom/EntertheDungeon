package com.graduationdesign.pxc.enterthedungeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.graduationdesign.pxc.enterthedungeon.util.OnContinueClickListener;
import com.graduationdesign.pxc.enterthedungeon.view.GameLayout;

public class GameActivity extends AppCompatActivity {
    private GameLayout mGameLayout;
    private View left;
    private View right;
    private View escgame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGameLayout = (GameLayout) findViewById(R.id.game);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        escgame=findViewById(R.id.escgame);
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
        escgame.setOnTouchListener(new OnContinueClickListener() {
            @Override
            public void handleClickEvent(View view) {
                Intent intent = new Intent(GameActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameLayout.stop();
    }

}
