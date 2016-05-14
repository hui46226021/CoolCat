package com.zshgif.laugh.acticty;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.zshgif.laugh.utils.RandomUtils;

import java.util.List;

/**
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 * Created by sh on 2015/9/9.
 */
public class ContextUtil extends Application implements Thread.UncaughtExceptionHandler {

    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
        instance = this;

    }


    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                AlertDialog.Builder builder = new AlertDialog.Builder(ContextUtil.this);
                builder.setTitle("提示");
                builder.setMessage(ex.getMessage()+"      是否发送错误报告");
                builder.setCancelable(false);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.create().show();
                Looper.loop();

            }
        }).start();



    }


   public String getImei(){
        SharedPreferences preferences2 = getSharedPreferences("imei", MODE_PRIVATE);
        String   imei  =    preferences2.getString("imei", "soujichuanhao");
       /**
        *      如果没有查找到 或者之前保存的IMEI是123456789
        *      就重新获取IMEI
        */

        if(imei.equals("soujichuanhao")){
            TelephonyManager mTm = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei   = mTm.getDeviceId()+ RandomUtils.getRandom();
            SharedPreferences.Editor editor = preferences2.edit();
            //设置参数
            editor.putString("imei", imei);
            //提交
            editor.commit();
        }
        return imei;
    }
}
