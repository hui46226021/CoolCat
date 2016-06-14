package com.zshgif.laugh.wechat.ui;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.zshgif.laugh.R;
import com.zshgif.laugh.cache.MapCache;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.db.InviteMessgeDao;
import com.zshgif.laugh.wechat.db.UserDao;


/**
 *聊天主界面
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    public static MainFragment instance;
    /**
     * 登录界面
     */
    private LoginFragment loginFragment;

    private int currentTabIndex;


    // 未读消息textview
    private TextView unreadLabel;
    // 未读通讯录textview
    private TextView unreadAddressLable;
    //按钮集合
    private Button[] mTabs;

    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private Fragment[] fragments;
    private ConversationListFragment conversationListFragment;
    private ContactListFragment contactListFragment;

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private int index;

    /**
     * 按钮布局
     */
    private LinearLayout buttonLayout;

    public MainFragment() {
        // Required empty public constructor
    }


    public synchronized static MainFragment newInstance() {
        if (instance == null){
            MainFragment fragment = new MainFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            instance = fragment;
        }

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getActivity().getPackageName();
            PowerManager pm = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                //intent.setAction(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        View view =   inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);

        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHelper.getInstance().logout(false,null);

            goLoginPage();
            return null;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理

            goLoginPage();
            return null;
        }


        /**
         * 被踢
         */
        if (getActivity().getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getActivity().getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            /**
             * 退出
             */
            showAccountRemovedDialog();
        }
        inviteMessgeDao = new InviteMessgeDao(getActivity());
        userDao = new UserDao(getActivity());
        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactListFragment();
        fragments = new Fragment[] { conversationListFragment, contactListFragment };
        // 添加显示第一个fragment
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(conversationListFragment)
                .commit();

        //注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();


        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //内部测试方法，请忽略
//        registerInternalDebugReceiver();
        Boolean is_login = (Boolean) MapCache.getObjectForKey(Constant.IS_LOGIN_KEY);
        if(!is_login){
            goLoginPage();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }
    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
//					unreadAddressLable.setText(String.valueOf(count));
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//        intentFilter.addAction(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if(contactListFragment != null) {
                        contactListFragment.refresh();
                    }
                }
                String action = intent.getAction();
//                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
//                    if (EaseCommonUtils.getTopActivity(getActivity()).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                    if (conversationListFragment != null){
//                        conversationListFragment.refresh();
//                    }
//                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 初始化组件
     */
    private void initView(View view) {
        unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) view.findViewById(R.id.unread_address_number);
        mTabs = new Button[3];
        mTabs[0] = (Button) view.findViewById(R.id.btn_conversation);

        mTabs[1] = (Button) view.findViewById(R.id.btn_address_list);
        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
        buttonLayout = (LinearLayout) view.findViewById(R.id.main_bottom);
    }




    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!getActivity().isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(getActivity());
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        goLoginPage();
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("", "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (getActivity().isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(getActivity());
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        goLoginPage();
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("", "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }
    /**
     * button点击事件
     *
     * @param
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_address_list:
                index = 1;
                break;


        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getChildFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(getActivity(), ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onContactAgreed(String username) {}
        @Override
        public void onContactRefused(String username) {}
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    public void loginSuccess(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonLayout.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                        .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(conversationListFragment)
                        .commit();
            }
        });





    }

    public void goLoginPage(){
        FragmentManager fm = getChildFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        loginFragment = LoginFragment.newInstance();
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.commit();
        buttonLayout.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
        unregisterBroadcastReceiver();


    }



}
