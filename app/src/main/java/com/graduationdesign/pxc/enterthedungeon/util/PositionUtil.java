package com.graduationdesign.pxc.enterthedungeon.util;

public class PositionUtil {
    public static int getRangeX(int screenWidth) {
        double rate = Math.random();
        //随机生成小于0.75的数值
        while (rate>=0.75){
            rate = Math.random();
        }
        return (int) (screenWidth * rate);
    }
}
