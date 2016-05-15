package com.zshgif.laugh.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/15.
 */
public class BaseFragment  extends Fragment {
    /**
     * 弹出吐丝
     * @param message
     */
    public void setToastMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
