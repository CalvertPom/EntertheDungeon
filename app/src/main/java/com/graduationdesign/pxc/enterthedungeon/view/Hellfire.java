package com.graduationdesign.pxc.enterthedungeon.view;


import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 *地形地狱火
 */
public class Hellfire extends Terrain{
    public Hellfire (int screenWidth, Paint paint, Bitmap bitmap) {
        super(screenWidth, paint,bitmap);
        setdHeight(60);
    }
}
