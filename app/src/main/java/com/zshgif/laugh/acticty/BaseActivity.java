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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        SharedPreferences preferences = getSharedPreferences("theme",0);
        switch (preferences.getInt("theme",1)){
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
    /**
     * 弹出吐丝
     * @param message
     */
    public void setToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
