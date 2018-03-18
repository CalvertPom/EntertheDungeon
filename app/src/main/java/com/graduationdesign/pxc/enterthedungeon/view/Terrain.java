package com.graduationdesign.pxc.enterthedungeon.view;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 地形类
 */

public class Terrain {
    //绘制的位置纵坐标
    public int mTerrainY;
    public int mTerrainX;
    //地形的宽度
    public int dWidth;

    public void setdHeight(int dHeight) {
        this.dHeight = dHeight;
    }

    //地形的高度
    public  int dHeight;
    // 当前的地形图片
    private Bitmap bitmap;

    //屏幕的宽度
    private int mScreenWidth;
    private Paint mPaint;


    public Terrain (int screenWidth,Paint paint, Bitmap bitmap) {
        this.mPaint = paint;
        this.mScreenWidth = screenWidth;
        this.dWidth = mScreenWidth;
        this.bitmap = bitmap;
    }
    public void drawTerrain(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(bitmap, null, new RectF(mTerrainX, mTerrainY , mTerrainX + dWidth,  mTerrainY+ dHeight), mPaint);
        canvas.restore();
    }
}
