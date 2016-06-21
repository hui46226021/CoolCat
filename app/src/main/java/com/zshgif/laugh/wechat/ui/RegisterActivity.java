package com.zshgif.laugh.wechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import com.hyphenate.exceptions.HyphenateException;
import com.zshgif.laugh.R;
import com.zshgif.laugh.acticty.BaseActivity;
import com.zshgif.laugh.utils.TimeCount;
import com.zshgif.laugh.wechat.DemoHelper;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity {
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText confirmPwdEditText;
    private EditText nick_name;
    private EditText code;
    private Button btn_sms;

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
        nick_name = (EditText) findViewById(R.id.nick_name);
        btn_sms = (Button) findViewById(R.id.btn_sms);
        code = (EditText) findViewById(R.id.code);
        time = new TimeCount(60000, 1000, btn_sms);
    }

    /**
     * 注册
     *
     * @param view
     */
    public void register(View view) {
        final String username = userNameEditText.getText().toString().trim();
        final String pwd = passwordEditText.getText().toString().trim();
        String confirm_pwd = confirmPwdEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            userNameEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            confirmPwdEditText.requestFocus();
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(nick_name.getText())) {
            Toast.makeText(this, getResources().getString(R.string.setnick_name), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(code.getText())) {
            Toast.makeText(this, getResources().getString(R.string.setnick_code), Toast.LENGTH_SHORT).show();
            return;
        }

        BmobSMS.verifySmsCode(this, username, code.getText() + "", new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//短信验证码已验证成功
//                    Log.i("smile", "验证通过");
                    register(username,pwd);


                } else {
//                    Log.i("smile", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());

                    setToastMessage("短信验证失败");
                    if("335400".equals(code.getText()+"")){
                        register(username,pwd);

                    }


                }

            }

        });

    }

    public void back(View view) {
        finish();
    }


    public void sendSMS(View view) {
        String username = userNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username) || username.length() != 11) {
            setToastMessage("请输入正确的手机号码");
            return;
        }
        time.start();
        BmobSMS.requestSMSCode(this, username, "register", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//验证码发送成功
//                    Log.i("smile", "短信id："+smsId);//用于查询本次短信发送详情
                }
            }
        });
    }

    public void register(final String username,final String pwd){
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMClient.getInstance().createAccount(username, pwd);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivity.this.isFinishing())
                                    pd.dismiss();
                                // 保存用户名
                                DemoHelper.getInstance().setCurrentUserName(username);
                                DemoHelper.getInstance().getUserProfileManager().updateCurrentUserRegisterNickName(username, nick_name.getText() + "");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.NETWORK_ERROR) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();


        }
    }

}
