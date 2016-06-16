package com.zshgif.laugh.wechat.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.acticty.BaseActivity;
import com.zshgif.laugh.cache.MapCache;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.adapter.PhoneContactsAdapter;
import com.zshgif.laugh.wechat.bean.PhoneConteacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhoneContactsActivity extends BaseActivity {
    List<PhoneConteacts> list = null;
    private Toolbar mToolbar;
    private ListView listView;
    private PhoneContactsAdapter phoneContactsAdapter;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private Dialog progressDialog;

    private static final int INT_QUERY = 1;

    private static final int END_QUERY = 2;

    private static final int START_QUERY = 3;

    private int progress =0;
    private  int maxCount=0;

    private int USER_OPEN=0;  //用户已经开启
    private int USER_UNOPEN=1; //用户未开启
    private int USER_AADDED = 2; //用户已添加
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contacts);
        initToolbar();
        listView = (ListView) findViewById(R.id.listview);
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("手机联系人");
        // 设置显示Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = (List<PhoneConteacts>) MapCache.getObjectForKey(Constant.PHONE_CONTACTS_LIST);
        if(list!=null){
            initListView();
            return;
        }

        list = new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取手机联系人
                /**获取库Phon表字段**/
                String[] PHONES_PROJECTION = new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
                maxCount =cursor.getCount();
                mHandler.sendEmptyMessage(START_QUERY);
                Map<String, EaseUser>  userMap =DemoHelper.getInstance().getContactList();

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
                        progress = progress+1;

                        mHandler.sendEmptyMessage(INT_QUERY);
                        LogUtils.e("添加一个",phoneConteacts.toString());
                    } while (cursor.moveToNext());
                    mHandler.sendEmptyMessage(END_QUERY);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            initListView();
                        }
                    });

                }
            }
        }).start();
    }

    void initListView(){
        phoneContactsAdapter = new PhoneContactsAdapter(PhoneContactsActivity.this,R.layout.item_phone_contacts,list);
        listView.setAdapter(phoneContactsAdapter);
    }

    /**
     *  添加contact
     * @param
     */
    public void addContact(final String username){
        if(EMClient.getInstance().getCurrentUser().equals(username)){
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if(DemoHelper.getInstance().getContactList().containsKey(username)){
            //提示已在好友列表中(在黑名单列表里)，无需添加
            if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(username)){
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        showProgressDialog("正在添加");

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(username, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            closeProgressDialog();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, 1).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            closeProgressDialog();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
                        }
                    });
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INT_QUERY:
                    mProgress.setProgress(progress);
                    break;
                case END_QUERY:

                    progressDialog.dismiss();
                    break;
                case START_QUERY:
                    showAlertDialog(maxCount);

                    break;
                default:
                    break;
            }
        };
    };

    void showAlertDialog(int maxCount){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("正在同步联系人数据");

        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);
        LogUtils.e("最大值",maxCount+"");
        mProgress.setMax(maxCount);
        builder.setView(v);
        progressDialog = builder.create();
        progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        progressDialog.show();
    }
    public void shareShow() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "我擦擦擦擦啊擦擦擦擦擦擦\n" +
                "我擦擦擦擦啊擦擦擦擦擦擦！\nhttp://www.baidu.com");
        intent.setType("text/plain");
        startActivity(intent);
    }
}
