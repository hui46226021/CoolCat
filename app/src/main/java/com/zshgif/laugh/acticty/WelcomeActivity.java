package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.zshgif.laugh.R;
import com.zshgif.laugh.cache.MapCache;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.bean.PhoneConteacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
                    //进入主页面
                    startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
                    MapCache.putObject(Constant.IS_LOGIN_KEY,true);
                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
                    MapCache.putObject(Constant.IS_LOGIN_KEY,false);
                    finish();
                }
            }
        }).start();



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
    }
}
