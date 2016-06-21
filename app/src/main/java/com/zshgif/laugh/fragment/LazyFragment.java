package com.zshgif.laugh.fragment;


import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * fragment的懒加载
 * Created by dxkj on 2015/10/29.
 */
public abstract class LazyFragment extends Fragment {

    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();
    protected abstract void unlazyLoad();
    protected void onInvisible(){
        unlazyLoad();
    }



}
