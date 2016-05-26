package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.hyphenate.easeui.controller.EaseUI;
import com.zshgif.laugh.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * activity基类
 * Created by zhush on 2016/5/12.
 */
public class BaseActivity extends AppCompatActivity {
    SharedPreferences preferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("theme",0);
        setMyTheme();
        super.onCreate(savedInstanceState);
    }

    /**
     * 点击空白页面收起输入法
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置主题
     */
    void setMyTheme(){

        switch (preferences.getInt("theme",2)){
            case 1:
                setTheme(R.style.MyThemeBlue);
                break;
            case 2:
                setTheme(R.style.MyThemeYellow);
                break;
            case 3:
                setTheme(R.style.MyThemePink);
                break;
            case 4:
                setTheme(R.style.MyThemeGreen);
                break;
        }
    }

    int setThemeColor1(){
        switch (preferences.getInt("theme",2)){
            case 1:
               return R.color.main_blue_dark;

            case 2:
                return R.color.main_yellow_dark;

            case 3:
                return R.color.main_pink_dark;

            case 4:
                return R.color.main_green_dark;

        }
        return R.color.main_blue_dark;
    }
    int setThemeColor2(){
        switch (preferences.getInt("theme",2)){
            case 1:
                return R.color.main_blue_light;

            case 2:
                return R.color.main_yellow_light;

            case 3:
                return R.color.main_pink_light;

            case 4:
                return R.color.main_green_light;

        }
        return R.color.main_blue_dark;
    }
    /**
     * 弹出吐丝
     * @param message
     */
    public void setToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
//        EaseUI.getInstance().getNotifier().reset();
    }
}
