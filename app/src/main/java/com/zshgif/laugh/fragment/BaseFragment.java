package com.zshgif.laugh.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/15.
 */
public class BaseFragment  extends Fragment {

    /**
     * listView显示在屏幕最上的
     */
    public  int FIRST_ONE;
    /**
     * listview显示在屏幕最下的
     */
    public  int LAST_ONE;
    /**
     * 弹出吐丝
     * @param message
     */
    public void setToastMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
