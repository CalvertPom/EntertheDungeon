package com.graduationdesign.pxc.enterthedungeon.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 背景乐开关
 */
public class Anniu {
    //绘制的位置纵坐标
    public float bY;
    public float bX;
    //地形的宽度
    public int bWidth;
    //按钮的高度
    public int bHeight;
    // 当前的地形图片
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Paint mPaint;

    public Anniu(Paint paint, Bitmap bitmap) {
        this.mPaint = paint;
        this.bWidth = bitmap.getWidth() * 3 / 2;
        this.bHeight = bitmap.getHeight() * 3 / 2;
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(bitmap, null, new RectF(bX, bY, bX + bWidth, bY + bHeight), mPaint);
        canvas.restore();
    }
}
