package com.zshgif.laugh.listener;

/**
 * HTTP请求回调
 * Created by sh on 2015/9/17.
 */
public interface HttpCallbackListener {
    void onHttpFinish(String response);
    void onHttpError(Exception e);
}
