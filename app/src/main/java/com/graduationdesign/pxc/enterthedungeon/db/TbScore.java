package com.graduationdesign.pxc.enterthedungeon.db;

import org.litepal.crud.DataSupport;

/*
 * 分数实体类
 */
public class TbScore extends DataSupport {
    private int _id;//存储分数编号
    private int score;//存储获得分数值
    private String time;//存储获得分数的时间

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
