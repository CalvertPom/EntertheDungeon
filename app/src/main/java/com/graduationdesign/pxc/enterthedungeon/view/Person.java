package com.graduationdesign.pxc.enterthedungeon.view;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Person {
    public static final int SPEED = 10;
    private int mHeaderRadius;
    private Paint mPaint;
    //下边缘坐标
    public int mPersonY;
    public int mPersonX;

    public void setmPersonY(int mPersonY) {
        this.mPersonY = mPersonY;
    }

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Person(Paint paint, int radius, Bitmap bitmap) {
        this.mPaint = paint;
        this.mHeaderRadius = radius;
        this.bitmap = bitmap;
    }


    /**
     * 绘制一个圆形图片
     */
    public void draw(Canvas canvas) {

        canvas.save();
        Path path = new Path();
        path.addCircle(mPersonX + mHeaderRadius, mPersonY + mHeaderRadius, mHeaderRadius * 3, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, null, new RectF(mPersonX, mPersonY, mPersonX + mHeaderRadius * 7 / 6, mPersonY + mHeaderRadius * 5 / 3), mPaint);
        canvas.restore();
    }

}
