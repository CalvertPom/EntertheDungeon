package com.graduationdesign.pxc.enterthedungeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button scoreboardButton;
    Button aboutautButton;
    Button bgstoryButton;
    Button escButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //数据库搭建
        Connector.getDatabase();
        startButton = (Button) findViewById(R.id.start);
        scoreboardButton = (Button) findViewById(R.id.score_board);
        bgstoryButton = (Button) findViewById(R.id.bgstory);
        aboutautButton= (Button) findViewById(R.id.aboutaut);
        escButton = (Button) findViewById(R.id.esc);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScoreBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        aboutautButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bgstoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackGroundStory.class);
                startActivity(intent);
                finish();
            }
        });
        escButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
