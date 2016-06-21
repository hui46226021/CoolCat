package com.zshgif.laugh.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.zshgif.laugh.R;

/**
 * Created by Administrator on 2016/5/15.
 */
public class BaseFragment  extends LazyFragment {
    SharedPreferences preferences ;
    /**
     * listView显示在屏幕最上的
     */
    public  int FIRST_ONE;
    /**
     * listview显示在屏幕最下的
     */
    public  int LAST_ONE;
    /**
     *余量
     */
    public static int ALLOWANCE =10;
    /**
     * 弹出吐丝
     * @param message
     */
    public boolean  initOk; //页面初始化完成

    public boolean loadOk;//加载完成
    /**
     * 页面状态
     */
    public Boolean pageState;

    public void setToastMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("theme",0);
    }

    @Override
    protected void lazyLoad() {
        pageState  =true;
    }

    @Override
    protected void unlazyLoad() {
        pageState  =false;
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

}
