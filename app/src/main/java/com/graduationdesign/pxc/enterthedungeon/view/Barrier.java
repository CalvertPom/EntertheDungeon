package com.graduationdesign.pxc.enterthedungeon.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * 障碍物
 */

public class Barrier {
    //绘制的位置纵坐标
    public int mPositionY;
    public int mPositionX;
    private Bitmap bitmap;
    //障碍物的宽度
    private int mWidth;
    //障碍物的高度
    private int mHeight;
    //屏幕的宽度
    private int mScreenWidth;

    private Paint mPaint;
    //当前的障碍物类型


    public Barrier(int screenWidth, Paint paint,Bitmap bitmap) {
        this.mScreenWidth = screenWidth;
        this.mPaint = paint;
        this.mWidth = mScreenWidth / 4;
        this.bitmap = bitmap;
    }

    /**
     * 绘制一个黑色矩形
     */
    public void drawBarrier(Canvas canvas) {
        canvas.save();
        Path path = new Path();
        path.addRect(mPositionX, mPositionY, mWidth + mPositionX, mPositionY + mHeight, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, null, new RectF(mPositionX, mPositionY, mWidth + mPositionX, mPositionY + mHeight), mPaint);
        canvas.restore();
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

}