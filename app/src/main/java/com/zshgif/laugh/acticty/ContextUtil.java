package com.zshgif.laugh.acticty;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.view.WindowManager;


import com.hyphenate.chat.EMOptions;
import com.squareup.leakcanary.LeakCanary;
import com.zshgif.laugh.dao.db.DaoMaster;
import com.zshgif.laugh.dao.db.DaoSession;
import com.zshgif.laugh.utils.RandomUtils;
import com.zshgif.laugh.wechat.DemoHelper;

/**
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 * Created by sh on 2015/9/9.
 */
public class ContextUtil extends Application  {

    private static ContextUtil instance;
    WindowManager windowManager;


    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static SQLiteDatabase db;
    //数据库名，表名是自动被创建的
    public static final String DB_NAME = "dbname.db";


    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //检查内存泄漏
//        LeakCanary.install(this);
//        Thread.setDefaultUncaughtExceptionHandler(this);
        instance = this;
        windowManager = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);


         windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度


        /**
         * 初始化环信
         */
        DemoHelper.getInstance().init(instance);

    }


//    @Override
//    public void uncaughtException(final Thread thread, final Throwable ex) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                AlertDialog.Builder builder = new AlertDialog.Builder(ContextUtil.this);
//                builder.setTitle("提示");
//                builder.setMessage(ex.getMessage()+"      是否发送错误报告");
//                builder.setCancelable(false);
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                });
//                builder.create().show();
//                Looper.loop();
//
//            }
//        }).start();
//
//
//
//    }


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

    /**
     * 获取屏幕宽都
     * @return
     */
    public int getScreenWidth(){
        return  windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public int getScreenHeight(){
        return  windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
    }


    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }


    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    public static SQLiteDatabase getSQLDatebase(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            db = daoMaster.getDatabase();
        }
        return db;
    }

}
