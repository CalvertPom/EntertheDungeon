package com.graduationdesign.pxc.enterthedungeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.graduationdesign.pxc.enterthedungeon.db.TbScore;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ScoreBoardActivity extends AppCompatActivity {
    public static final String FLAG = "id";// 定义一个常量，用来作为请求码
    ListView lvinfo;// 创建ListView对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score_board);
        lvinfo = (ListView) findViewById(R.id.lvinscoreinfo);// 获取布局文件中的ListView组件
        ShowInfo();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(ScoreBoardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void ShowInfo() {// 用来根据传入的管理类型，显示相应的信息
        String[] strInfos = null;// 定义字符串数组，用来存储分数信息
        ArrayAdapter<String> arrayAdapter = null;// 创建ArrayAdapter对象
        // 获取所有分数信息，并存储到List泛型集合中
        List<TbScore> listinfos = DataSupport.order("score desc").limit(10).find(TbScore.class);
        strInfos = new String[listinfos.size()];// 设置字符串数组的长度
        int m = 0;// 定义一个开始标识
        for (TbScore tb_score : listinfos) {// 遍历List泛型集合
            // 将分数相关信息组合成一个字符串，存储到字符串数组的相应位置
            strInfos[m] = tb_score.getTime() + "             " + String.valueOf(tb_score.getScore()) + "分";
            m++;// 标识加1
        }
        // 使用字符串数组初始化ArrayAdapter对象
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strInfos);
        lvinfo.setAdapter(arrayAdapter);// 为ListView列表设置数据源

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();// 实现基类中的方法
        ShowInfo();// 显示分数信息
    }
}
