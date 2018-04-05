package com.graduationdesign.pxc.enterthedungeon.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.graduationdesign.pxc.enterthedungeon.R;
import com.graduationdesign.pxc.enterthedungeon.db.TbScore;
import com.graduationdesign.pxc.enterthedungeon.util.Music;
import com.graduationdesign.pxc.enterthedungeon.util.PositionUtil;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 游戏布局
 */
public class GameLayout extends View {
    //当前视图(GameLayout)的长和宽
    private int mLayoutWidth;
    private int mLayoutHeight;
    //辅助绘制障碍物的对象
    private Barrier mBarrier;
    //辅助绘制人物的对象
    private Person mPerson;
    //面板绘制的对象
    private Score mScore;
    //画笔
    private Paint mPaint;
    //小人的圆形半径
    private int radius = 50;
    //不断绘制的线程
    private Thread mThread;
    private MyHandler myHandler;
    //障碍上升加速度
    private int mBarrierMoveSpeed = 10;//8简单10普通12难15炼狱
    //地形
    private Spike mSpike;//顶上刺
    private Bitmap bSpike = BitmapFactory.decodeResource(getResources(), R.drawable.up);//顶上刺
    //人物是否自动下落状态
    private boolean isAutoFall;
    //人物左右移动的速度
    private int mPersonMoveSpeed = 10;
    //游戏是否正在运行
    private boolean isRunning;
    private Bitmap bmans = BitmapFactory.decodeResource(getResources(), R.drawable.jerryd1);
    private Bitmap bmanl1 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryl1);//左
    private Bitmap bmanl2 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryl2);//左
    private Bitmap bmanl3 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryl3);//左
    private Bitmap bmanl4 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryl4);//左
    private Bitmap bmanr1 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryr1);//右
    private Bitmap bmanr2 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryr2);//右
    private Bitmap bmanr3 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryr3);//右
    private Bitmap bmanr4 = BitmapFactory.decodeResource(getResources(), R.drawable.jerryr4);//右
    private Bitmap bmand = BitmapFactory.decodeResource(getResources(), R.drawable.jerrydead);//输了
    //需要绘制的障碍
    private Bitmap bitplat;
    private Bitmap bitplatd;//加速
    //当前的障碍物类型
    private int mType = 1;
    //画面中障碍物的位置信息
    private ArrayList<Integer> mBarrierXs;
    private ArrayList<Integer> mBarrierYs;
    //画面中障碍物的类型信息
    private ArrayList<Integer> mBarrierTs;
    //障碍物起始和产生障碍的间隔
    private int mBarrierStartY = 500;
    private int mBarrierInterval = 500;
    //障碍物的高度
    private int mBarrierHeight = 60;
    //人物所站立的障碍在画面中的index
    private int mTouchIndex = -1;
    //当小人自动下落瞬间，开始计时，单位毫秒
    private float mFallTime = 0;
    //重力加速度
    public static final float G = 9.8f;
    //特殊砖加速
    int acc = 0;
    //总得分
    private int mTotalScore;
    //是否记录得分
    private boolean isScore;
    //份数版块的文字大小
    private int mTextSize = 16;
    //失败后，弹出的菜单，按钮的位置
    private RectF mRestartRectf;
    private RectF mQuiteRectf;
    //按钮的宽度和高度，这里我省事没有转化为DP，都是直接用px，所以可能会产生适配上的问题。
    private int mButtonWidth = 300;
    private int mButtonHeight = 120;
    private int Padding = 20;
    //屏幕密度
    private float density = getResources().getDisplayMetrics().density;
    //音乐对象
    private Music bgm;
    //音乐暂停
    //暂停按钮的对象
    private RectF mBBPRectf;
    private Anniu mButtonbgmPause;
    //暂停图标
    private Bitmap stopm = BitmapFactory.decodeResource(getResources(), R.drawable.startm);//暂停状态
    private Bitmap startm = BitmapFactory.decodeResource(getResources(), R.drawable.stopm);//播放状态
    //音乐状态
    public static final int STATUS_BGM_STARTED = 1;//音乐开始
    public static final int STATUS_BGM_PAUSED = 2;//音乐暂停
    public static final int STATUS_BGM_STILL = 3;//音乐在播
    private int status = STATUS_BGM_STARTED;//初始为音乐开始状态

    public GameLayout(Context context) {
        super(context);
        init();
    }

    public GameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(10);
        //读取障碍的图片
        bitplat = BitmapFactory.decodeResource(getResources(), R.drawable.plat);
        bitplatd = BitmapFactory.decodeResource(getResources(), R.drawable.acc);
        //默认开始自动下落
        isAutoFall = true;
        myHandler = new MyHandler();
        //用来记录画面中，每一个障碍物的x坐标
        mBarrierXs = new ArrayList<>();
        //和上面的x对应的每个障碍物的y坐标
        mBarrierYs = new ArrayList<>();
        //用来记录画面中，每一个障碍物的类型
        mBarrierTs = new ArrayList<>();
        //将文字大小转化成DP
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics());
        //启动游戏
        isRunning = true;
        isScore = false;
        startGame();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //当前方法，是在onMeasure调用之后，进行回调，所以直接getMeasureWidth等
        //获取当前视图的宽和高
        mLayoutWidth = getMeasuredWidth();
        mLayoutHeight = getMeasuredHeight();
        //根据视图宽高，初始化障碍物的信息
        mBarrier = new Barrier(mLayoutWidth, mPaint, bitplat);
        mBarrier.setHeight(mBarrierHeight);
        //创建人物绘制类对象
        mPerson = new Person(mPaint, radius, bmans);
        mPerson.mPersonY = 300;
        mPerson.mPersonX = mLayoutWidth / 2;

        //绘制左上角的暂停按钮

        mButtonbgmPause = new Anniu(mPaint, startm);
        mBBPRectf = new RectF(15 * density, 15 * density, 15 * density + mButtonbgmPause.bWidth, 15 * density + mButtonbgmPause.bHeight);

        //初始化地形对象
        mSpike = new Spike(mLayoutWidth, mPaint, bSpike);//顶上刺
        //mHellfire = new Hellfire(mLayoutWidth, mPaint, bHellfire);//地狱火
        mSpike.mTerrainX = 0;
        // mHellfire.mTerrainX = 0;
        mSpike.mTerrainY = 0;
        //  mHellfire.mTerrainY = mLayoutHeight - mHellfire.dHeight;

        //初始化分数绘制对象
        mScore = new Score(mPaint);
        mScore.x = mLayoutWidth / 2 - mScore.panelWidth / 2;

        //菜单上重启按钮的左边坐标,mRestartRectf是重启按钮绘制区域
        int rX = mLayoutWidth / 2 - 20 - mButtonWidth;
        int rY = mLayoutHeight * 3 / 5;
        mRestartRectf = new RectF(rX, rY, rX + mButtonWidth, rY + mButtonHeight);
        //下面是菜单上退出按钮的区域
        int qX = mLayoutWidth / 2 + 20;
        int qY = mLayoutHeight * 3 / 5;
        mQuiteRectf = new RectF(qX, qY, qX + mButtonWidth, qY + mButtonHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        generateTerrain(canvas);
        //绘制分数面板
        generateScore(canvas);
        //绘制障碍物
        generateBarrier(canvas);
        drawbBGM(canvas);
        //如果小人正在下落，才检测是否碰撞
        if (isAutoFall)
            checkTouch();
        //根据是否下落，绘制小人的位置
        generatePerson(canvas);
        //如果没有结束，说明就是在运行
        //检查小人是否超出边界，判断游戏是否结束
        isRunning = !checkIsGameOver();
        //如果游戏结束

        if (!isRunning) {
            mPerson.setBitmap(bmand);
            //绘制面板
            drawPanel(canvas);
            //绘制游戏结束数字
            notifyGameOver(canvas);
            //绘制两个按钮
            drawButton(canvas, mRestartRectf, "重来", Color.parseColor("#ae999999"), Color.WHITE);
            drawButton(canvas, mQuiteRectf, "退出", Color.parseColor("#ae999999"), Color.WHITE);
        }
    }

    //绘制bgm按钮
    private void drawbBGM(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.DKGRAY);
        mButtonbgmPause.bX = 15 * density;
        mButtonbgmPause.bY = 15 * density;
        mButtonbgmPause.draw(canvas);
    }

    /**
     * 绘制结束弹出框的背景区域
     */
    private void drawPanel(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#8e333333"));
        canvas.drawRoundRect(new RectF(mRestartRectf.left - Padding * 5 / 2, mLayoutHeight * 2 / 5 - Padding, mQuiteRectf.right + Padding * 5 / 2, mQuiteRectf.bottom + Padding), Padding, Padding, mPaint);
    }

    /**
     * 绘制Game over文字
     */
    private void notifyGameOver(Canvas canvas) {

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize * 1.5f);
        mPaint.setColor(Color.parseColor("#cc0000"));
        mPaint.setFakeBoldText(false);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);//设置抗锯齿，否则斜线等会有锯齿
        mPaint.setSubpixelText(true);//设置亚像素，可以让文字更加清晰明显，对文本设置的一种优化
        canvas.drawText("游戏结束！！！", mLayoutWidth / 2, mLayoutHeight / 2, mPaint);
        canvas.drawText("您本次的得分为:" + mTotalScore + "分", mLayoutWidth / 2, mLayoutHeight / 2 + mTextSize * 1.5f, mPaint);
        String strInScore = String.valueOf(mTotalScore);
        if (!strInScore.isEmpty() && isScore == false) {// 判断分数不为空
            // scoreDAO.add(tb_score);// 添加本次得分信息
            TbScore tbscore = new TbScore();
            tbscore.setScore(Integer.parseInt(strInScore));
            tbscore.setTime(getSysNowTime());
            tbscore.save();
            isScore = true;
            // 弹出信息提示
           // Toast.makeText(getContext(), "〖新增分数〗数据添加成功！", Toast.LENGTH_SHORT).show();
        }
        String[] strInfos = null;// 定义字符串数组，用来存储分数信息
// 获取所有分数信息，并存储到List泛型集合中
        List<TbScore> listinfos = DataSupport.order("score desc").find(TbScore.class);
        strInfos = new String[listinfos.size()];// 设置字符串数组的长度
        int m = 0;// 定义一个开始标识
        for (TbScore tb_score : listinfos) {// 遍历List泛型集合
            strInfos[m] = String.valueOf(tb_score.getScore());
            m++;// 标识加1
        }
        canvas.drawText("历史最高成绩:" + strInfos[0] + "分", mLayoutWidth / 2, mLayoutHeight / 2 + mTextSize * 3f, mPaint);
    }

    // 获取现在系统时间
    public String getSysNowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    //绘制菜单按钮,下面的操作使得文字能够居中显示
    private void drawButton(Canvas canvas, RectF rectF, String text, int strokeColor, int textColor) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(strokeColor);
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(textColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        int y = (int) (rectF.top + textHeight / 2 + (rectF.bottom - rectF.top) / 2 - fontMetrics.bottom);
        canvas.drawText(text, rectF.left + mButtonWidth / 2, y, mPaint);

    }

    /**
     * 绘制地形
     */
    private void generateTerrain(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#666666"));
        // mHellfire.drawTerrain(canvas);
        mSpike.drawTerrain(canvas);
    }

    /**
     * 绘制分数面板
     */
    private void generateScore(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#666666"));
        mScore.drawPanel(canvas);
        mPaint.setColor(Color.WHITE);
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize(mTextSize);
        mScore.drawScore(canvas, mTotalScore + "");
    }

    private void generateBarrier(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.DKGRAY);
        //每次都清楚Y坐标信息，因为后面会重新生成
        mBarrierYs.clear();
        //死循环，有条件退出
        for (int i = 0; ; ) {
            //i小于数组中的长度，那么取出原有的x位置信息，绘制旧障碍物;
            // 否则就随机生成新的坐标信息添加到数组中
            if (i < mBarrierXs.size()) {
                mBarrier.mPositionX = mBarrierXs.get(i);
                mBarrier.mType = mBarrierTs.get(i);
                if (mBarrier.mType == 7) {//加速砖
                    mBarrier.setBitmap(bitplatd);
                } else if (mBarrier.mType != 7) {//普通砖
                    mBarrier.setBitmap(bitplat);
                }

            } else {
                mBarrier.mPositionX = PositionUtil.getRangeX(mLayoutWidth);
                mBarrier.mType = PositionUtil.getRangeT();//设置Barrier类型随机数
                mBarrierXs.add(mBarrier.mPositionX);
                mBarrierTs.add(mBarrier.mType);
            }
            //障碍物的y坐标
            mBarrier.mPositionY = mBarrierStartY + mBarrierInterval * i;
            mBarrierYs.add(mBarrier.mPositionY);

            //绘制到视图外，则不再进行绘制，退出循环
            if (mBarrier.mPositionY > mLayoutHeight) {
                break;
            }
            mBarrier.drawBarrier(canvas);
            i++;
        }
    }


    private void generatePerson(Canvas canvas) {
        //如果小人在自动下落
        if (isAutoFall) {//落
            //自动下落绘制
            // mFallTime += 20;
            mFallTime += 40;
            mFallTime += acc;
            //根据重力加速度计算小人下落的位置
            mPerson.mPersonY += mFallTime / 1000 * G;
            mPerson.draw(canvas);
        } else {//不落

            //小人被挡住，下落的时间重置
            mFallTime = 0;
            //mTouchIndex表示的是小人在视图中被阻挡的的障碍物的位置
            //如果是小于0,表示没有阻挡,
            if (mTouchIndex >= 0) {
                //设置小人被阻挡的位置，被进行绘制
                mPerson.mPersonY = mBarrierYs.get(mTouchIndex) - radius * 5 / 3;
                mPerson.draw(canvas);
            }
        }
    }


    /**
     * 碰撞检测
     */
    private void checkTouch() {
        for (int i = 0; i < mBarrierYs.size(); i++) {
            //碰撞检测
            if (isTouchBarrier(mBarrierXs.get(i), mBarrierYs.get(i))) {
                mTouchIndex = i;
                if (mBarrierTs.get(i) != 7) {
                    acc = 0;
                    isAutoFall = false;//不落
                } else {
                    if (mBarrierTs.get(i) == 7) {
                        acc = 520;
                        isAutoFall = true;
                    }
                }
            }
        }
    }

    //操作是否完成
    private boolean checkIsGameOver() {
        if (mPerson.getBitmap().equals(bmanr1) || mPerson.getBitmap().equals(bmanr2) || mPerson.getBitmap().equals(bmanr3) || mPerson.getBitmap().equals(bmanr4)) {
            mPerson.setBitmap(bmanr1);
        } else if (mPerson.getBitmap().equals(bmanl1) || mPerson.getBitmap().equals(bmanl2) || mPerson.getBitmap().equals(bmanl3) || mPerson.getBitmap().equals(bmanl4)) {
            mPerson.setBitmap(bmanl1);
        }
        return mPerson.mPersonY < 120 || mPerson.mPersonY > mLayoutHeight - radius * 5 / 3;
    }

    /**
     * 碰撞检测
     */
    private boolean isTouchBarrier(int x, int y) {
        boolean res = false;
        double pY = mPerson.mPersonY + radius * 5 / 3;
        //在瞬间刷新的时候，只要小人的位置和障碍的位置，差值在小人和障碍物的瞬间刷新的最大值就属于碰撞
        //比如：小人下落速度为a,障碍物上升速度为b,画面刷新时间为t,瞬间刷新，会有个最大差值，这个值就是
        //临界值
        if (Math.abs(pY - y) <= Math.abs(mBarrierMoveSpeed + mBarrier.getmHeight() + Person.SPEED + mFallTime / 1000 * G)) {
            if (mPerson.mPersonX + radius * 7 / 6 >= x && mPerson.mPersonX <= x + mBarrier.getWidth()) {
                res = true;
            }
        }
        return res;
    }

    private void initbgm() {
        bgm.play(getContext(), R.raw.bgm4);
        status = STATUS_BGM_STILL;
    }

    private void bgmStart() {
        mButtonbgmPause.setBitmap(startm);
        bgm.play(getContext(), R.raw.bgm4);
        status = STATUS_BGM_STILL;//=3;//音乐在播
    }

    private void bgmStop() {
        mButtonbgmPause.setBitmap(stopm);
        bgm.stop(getContext());
        status = STATUS_BGM_PAUSED;
    }

    public void startGame() {
        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (status == STATUS_BGM_STARTED)//音乐开始
                {
                    initbgm();
                }
                while (isRunning) {
                    //开始让障碍往上面滚动,障碍物的绘制，是跟mBarrierStartY相关的
                    mBarrierStartY -= mBarrierMoveSpeed;
                    //当第一个障碍物开始消失
                    if (mBarrierStartY <= -mBarrierInterval - mBarrierHeight) {
                        mBarrierStartY = -mBarrierHeight;
                        //删除刚消失的障碍物坐标信息
                        if (mBarrierXs.size() > 0) {
                            mBarrierXs.remove(0);
                            mBarrierTs.remove(0);
                        }
                        //得分++
                        mTotalScore++;
                        // 碰撞位置--
                        mTouchIndex--;
                    }
                    //这里应该是可以直接用postInvalidate()
                    myHandler.sendEmptyMessage(0x1);
                    try {
                        //每20毫秒刷新一次界面
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                invalidate();
            }

        }
    }


    //控制小人向左移动
    public void moveLeft() {
        if (isRunning) {
            double x = mPerson.mPersonX;
            if (mPerson.getBitmap().equals(bmanl1)) {
                mPerson.setBitmap(bmanl2);
            } else if (mPerson.getBitmap().equals(bmanl2)) {
                mPerson.setBitmap(bmanl3);
            } else if (mPerson.getBitmap().equals(bmanl3)) {
                mPerson.setBitmap(bmanl4);
            } else {
                mPerson.setBitmap(bmanl1);
            }
            double dir = x - mPersonMoveSpeed;
            if (dir < 0)
                dir = 0;
            mPerson.mPersonX = dir;
            //移动过程中，启动边界检测,设置isAutoFall为true
            checkIsOutSide(dir);
            invalidate();
        }
    }

    /**
     * 类似moveLeft
     */
    public void moveRight() {
        if (isRunning) {
            double x = mPerson.mPersonX;
            if (mPerson.getBitmap().equals(bmanr1)) {
                mPerson.setBitmap(bmanr2);
            } else if (mPerson.getBitmap().equals(bmanr2)) {
                mPerson.setBitmap(bmanr3);
            } else if (mPerson.getBitmap().equals(bmanr3)) {
                mPerson.setBitmap(bmanr4);
            } else {
                mPerson.setBitmap(bmanr1);
            }
            double dir = x + mPersonMoveSpeed;
            if (dir > mLayoutWidth - radius * 5 / 3)
                dir = mLayoutWidth - radius * 5 / 3;
            mPerson.mPersonX = dir;
            checkIsOutSide(dir);
            invalidate();
        }
    }

    //移动超出板子
    private void checkIsOutSide(double x) {
        isAutoFall = true;
    }

    public void stop() {
        isRunning = false;
    }

    //游戏销毁清空资源
    public void destory() {
        stop();
        bgmStop();
        mTotalScore = 0;
        isAutoFall = false;
        acc = 0;
        mFallTime = 0;
        mBarrierXs.clear();
        mBarrierYs.clear();
        mBarrierTs.clear();
        mBarrierStartY = 500;
        if (mPerson != null) {
            mPerson.destroy();
        }
        if (mBarrier != null) {
            mBarrier.destroy();
        }
        mPerson = null;
        mBarrier = null;
        bmans.recycle();
        bmanl1.recycle();
        bmanl2.recycle();
        bmanl3.recycle();
        bmanl4.recycle();
        bmanr1.recycle();
        bmanr2.recycle();
        bmanr3.recycle();
        bmanr4.recycle();
        bmand.recycle();
        bSpike.recycle();
        bitplat.recycle();
        bitplatd.recycle();
        mThread.interrupt();
        mThread = null;
        System.exit(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //按下坐标
        float downx = mLayoutWidth / 2;
        float downy = 0;
        //离开坐标
        float upx = 0;
        float upy = 0;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //获取触摸位置信息
            downx = event.getX();
            downy = event.getY();
            if (mBBPRectf.contains(downx, downy)) {
                if (status == STATUS_BGM_PAUSED)//音乐开始
                {
                    bgmStart();
                } else if (status == STATUS_BGM_STILL) {
                    bgmStop();
                }
            }
            if (isRunning) {
                if (mLayoutWidth / 2 - downx > 0) {
                    moveLeft();
                } else if (mLayoutWidth / 2 - downx < 0) {
                    moveRight();
                }
            }
            //游戏正在运行，没有生成菜单，执行控制器
            if (!isRunning) {
                //如果触摸到重启游戏的按钮，触发
                if (mRestartRectf.contains(downx, downy)) {
                    restartGame();
                } else if (mQuiteRectf.contains(downx, downy)) {//触摸到退出按钮
                    //System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            upx = event.getX();
            if (downx - upx > 0) {
                //Toast.makeText(getContext(), "左x="+downx+",upx="+upx, Toast.LENGTH_SHORT).show();
                moveLeft();
            } else if (downx - upx < 0) {
                //  Toast.makeText(getContext(), "右x="+downx+",upx="+upx, Toast.LENGTH_SHORT).show();
                moveRight();
            }
        }

        return true;
        // return super.onTouchEvent(event);
    }

    /**
     * 重置游戏信息
     */
    private void restartGame() {
        mBarrierXs.clear();
        mBarrierYs.clear();
        mBarrierTs.clear();
        mBarrierStartY = 500;
        mPerson.setBitmap(bmans);
        mPerson.mPersonY = 300;
        mPerson.mPersonX = mLayoutWidth / 2;
        mTotalScore = 0;
        isAutoFall = true;
        acc = 0;
        mFallTime = 0;
        isRunning = true;
        isScore = false;
        startGame();
    }
}