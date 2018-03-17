package com.graduationdesign.pxc.enterthedungeon.util;

import android.content.Context;
import android.media.MediaPlayer;
/**
 * 用于背景音乐
 */
public class Music {
    private static MediaPlayer mp = null;	//声明一个MediaPlayer对象

    public static void play(Context context, int resource) {
        mp = MediaPlayer.create(context, resource);
        mp.setLooping(true); // 是否循环播放
        mp.start(); // 开始播放
    }

    public static void stop(Context context) {
        if (mp != null) {
            mp.stop();		//停止播放
            mp.release();	//释放资源
            mp = null;
        }

    }
}