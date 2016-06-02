package com.zshgif.laugh.wechat.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.fragment.LazyFragment;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.db.DemoDBManager;

/**
 *
 * 登录页面
 */
public class LoginFragment extends LazyFragment implements View.OnClickListener{
    /**
     *
     */
    @ViewInject(R.id.username)
    private EditText usernameEditText;
    @ViewInject(R.id.password)
    private EditText passwordEditText;
    @ViewInject(R.id.login_button)
    private Button login_button;
    @ViewInject(R.id.register_button)
    private Button register_button;
    String Tag ="LoginFragment";

    /**
     * 进度条
     */
    private boolean progressShow;
    private boolean autoLogin = false;

    private String currentUsername;
    private String currentPassword;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.fragment_login, container, false);
        ViewUtils.inject(this, view);
        init();
        return view;
    }

    /**
     * 初始化
     */

    void init(){
        login_button.setOnClickListener(this);
        register_button.setOnClickListener(this);

        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /**
         * 如果有当前用户  放上当前用户的账户
         */
        if (DemoHelper.getInstance().getCurrentUsernName() != null) {
            usernameEditText.setText(DemoHelper.getInstance().getCurrentUsernName());
        }

    }

    /**
     * 登录
     *
     * @param
     */
    public void login() {
        //检查网络
        if (!EaseCommonUtils.isNetWorkConnected(getActivity())) {
            Toast.makeText(getActivity(), R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(getActivity(), R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(getActivity(), R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        Log.d(Tag, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(Tag, "login: onSuccess");


                    pd.dismiss();

                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        ContextUtil.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                //TODO  登录后操作
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(Tag, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(Tag, "login: onError: " + code);
                if (!progressShow) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }





    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void unlazyLoad() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                login();
                break;
            case R.id.register_button:
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), 0);
                break;
        }
    }
}
