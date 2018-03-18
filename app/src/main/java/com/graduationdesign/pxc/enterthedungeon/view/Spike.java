package com.graduationdesign.pxc.enterthedungeon.view;


import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 * 顶上刺
 */
public class Spike extends Terrain {
    public Spike(int screenWidth, Paint paint, Bitmap bitmap) {
        super(screenWidth, paint, bitmap);
        setdHeight(120);
    }
}
