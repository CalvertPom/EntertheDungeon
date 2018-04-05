package com.graduationdesign.pxc.enterthedungeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class BackGroundStory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_back_ground_story);


        GifImageView gifImageView1 = (GifImageView) findViewById(R.id.gif1);

        try {
            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
            // 将gif图资源转化为GifDrawable
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.backgroudstory);

            // gif1加载一个动态图gif
            gifImageView1.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button button = (Button) findViewById(R.id.pass);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(BackGroundStory.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(BackGroundStory.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
