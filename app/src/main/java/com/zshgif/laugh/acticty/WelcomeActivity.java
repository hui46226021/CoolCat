package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.hyphenate.chat.EMClient;
import com.zshgif.laugh.R;
import com.zshgif.laugh.cache.MapCache;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.wechat.DemoHelper;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    public static WelcomeActivity instance;
    private static final int sleepTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        instance =this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            public void run() {
//                if (DemoHelper.getInstance().isLoggedIn()) {
//                    // ** 免登陆情况 加载所有本地群和会话
//                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
//                    //加上的话保证进了主页面会话和群组都已经load完毕
//                    long start = System.currentTimeMillis();
//                    EMClient.getInstance().groupManager().loadAllGroups();
//                    EMClient.getInstance().chatManager().loadAllConversations();
//                    long costTime = System.currentTimeMillis() - start;
//                    //等待sleeptime时长
//                    if (sleepTime - costTime > 0) {
//                        try {
//                            Thread.sleep(sleepTime - costTime);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    //进入主页面
//                    startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
//                    MapCache.putObject(Constant.IS_LOGIN_KEY,true);
//                    finish();
//                }else {
//                    try {
//                        Thread.sleep(sleepTime);
//                    } catch (InterruptedException e) {
//                    }
//                    startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
//                    MapCache.putObject(Constant.IS_LOGIN_KEY,false);
//                    finish();
//                }
                startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
            }
        }).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
    }
}
