package com.zshgif.laugh.wechat.utils;

import com.zshgif.laugh.wechat.bean.hxuser;

import java.util.List;

/**
 * Created by zhush on 2016/6/22.
 */
public interface EMVhxuserCallBack {
    void onSuccess(List<hxuser> hxuserlist,boolean isfinish);
}
