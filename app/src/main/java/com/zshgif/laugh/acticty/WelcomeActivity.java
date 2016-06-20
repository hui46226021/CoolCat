package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.zshgif.laugh.R;
import com.zshgif.laugh.cache.MapCache;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.bean.PhoneConteacts;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SplashView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.Vitamio;

/**
 * 启动也
 */
public class WelcomeActivity extends AppCompatActivity {
    public static WelcomeActivity instance;
    private static final int sleepTime = 2000;
    List<PhoneConteacts> list = new ArrayList<>();
    private int USER_OPEN=0;  //用户已经开启
    private int USER_UNOPEN=1; //用户未开启
    private int USER_AADDED = 2; //用户已添加
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        instance =this;
        /**
         * 获取审核状态
         */
        DemoHelper.getInstance().getUserProfileManager().getExamineState();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取手机联系人
                /**获取库Phon表字段**/
                String[] PHONES_PROJECTION = new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
                Map<String, EaseUser> userMap =DemoHelper.getInstance().getContactList();
                if (cursor.moveToFirst()) {
                    do {
                        /**联系人显示名称**/
                        int PHONES_DISPLAY_NAME_INDEX = 0;
                        /**电话号码**/
                        int PHONES_NUMBER_INDEX = 1;
                        String phone = cursor.getString(PHONES_NUMBER_INDEX);
                        String contactName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
                        int state = 0;
                        if(DemoHelper.getInstance().getUserProfileManager().isUserRegister(phone)){
                            state =   userMap.containsKey(phone)?USER_AADDED:USER_OPEN;
                        }else {
                            state =USER_UNOPEN;
                        }
                        PhoneConteacts phoneConteacts = new PhoneConteacts(phone.replace("+86",""),contactName,state);
                        if (state==USER_OPEN){
                            list.add(0,phoneConteacts);
                        }else {
                            list.add(phoneConteacts);
                        }


                        LogUtils.e("添加一个",phoneConteacts.toString());
                    } while (cursor.moveToNext());
                    MapCache.putObject(Constant.PHONE_CONTACTS_LIST,list);
                }
            }
        }).start();
    }


    @Override
    protected void onStart() {
        super.onStart();

        /**
         * 有米广告
         * appId 和 appSecret 分别为应用的发布 ID 和密钥，由有米后台自动生成，通过在有米后台 > 应用详细信息 可以获得。
         isTestModel : 是否开启测试模式，true 为是，false 为否。（上传有米审核及发布到市场版本，请设置为 false）
         */
        AdManager.getInstance(this).init(Constant.YM_APP_ID,Constant.YM_APP_SIGN,Constant.YM_STATE);
        SpotManager.getInstance(instance).loadSpotAds();
        setupSplashAd();
        /**
         * 初始化视频播放
         */
        Vitamio.isInitialized(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    MapCache.putObject(Constant.IS_LOGIN_KEY,true);
                    //进入主页面
                    /**
                     * 通过有米广告进入主页了
                     */
//                    startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
//
//                    finish();
                }else {
                    MapCache.putObject(Constant.IS_LOGIN_KEY,false);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
//                    startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
//
//                    finish();
                }
            }
        }).start();


    }



    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        /**
         * 自定义模式
         */
        SplashView splashView = new SplashView(this, null);
        // 设置是否显示倒计时，默认显示
        splashView.setShowReciprocal(true);
        // 设置是否显示关闭按钮，默认不显示
        splashView.hideCloseBtn(true);
//        //传入跳转的intent，若传入intent，初始化时目标activity应传入null
        Intent intent = new Intent(this, MyActivity.class);
        splashView.setIntent(intent);
        //展示失败后是否直接跳转，默认直接跳转
        splashView.setIsJumpTargetWhenFail(true);
        //获取开屏视图
        View splash = splashView.getSplashView();

        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
        //		splashLayout.setVisibility(View.GONE);
        //添加开屏视图到布局中
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.view_divider);
        splashLayout.addView(splash, params);
        //显示开屏
        SpotManager.getInstance(this)
                .showSplashSpotAds(this, splashView, new SpotDialogListener() {

                    @Override
                    public void onShowSuccess() {
                        LogUtils.e("dd", "开屏展示成功");
                        splashLayout.setVisibility(View.VISIBLE);
                        splashLayout.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.anim_splash_enter));
                    }

                    @Override
                    public void onShowFailed() {
                        Log.i("", "开屏展示失败");
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.i("", "开屏被关闭");
                    }

                    @Override
                    public void onSpotClick(boolean isWebPath) {
                        Log.i("", "开屏被点击");
                    }
                });

        /**
         * 默认模式
         */
        // SpotManager.getInstance(this).showSplashSpotAds(this,
        // MainActivity.class);
    }
}
