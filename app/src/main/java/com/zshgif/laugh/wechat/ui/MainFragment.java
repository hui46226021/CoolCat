package com.zshgif.laugh.wechat.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zshgif.laugh.R;


/**
 *聊天主界面
 */
public class MainFragment extends Fragment {

    public static MainFragment instance;
    /**
     * 登录界面
     */
    private LoginFragment loginFragment;

    private BlankFragment blankFragment;


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


     View view =   inflater.inflate(R.layout.fragment_main, container, false);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        loginFragment = LoginFragment.newInstance();
        transaction.replace(R.id.id_fragment_title, loginFragment);
        transaction.commit();
    }


    public void loginSuccess(){

        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        blankFragment = BlankFragment.newInstance();
        transaction.replace(R.id.id_fragment_title, blankFragment);
        transaction.commit();
    }
}
