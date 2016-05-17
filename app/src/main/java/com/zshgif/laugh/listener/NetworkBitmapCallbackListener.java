package com.zshgif.laugh.listener;

/**
 * 网络图片请求回调
 * Created by sh on 2015/9/17.
 */
public interface NetworkBitmapCallbackListener {
    void onHttpFinish(byte[] bytes);
    void onHttpError(Exception e);
}
