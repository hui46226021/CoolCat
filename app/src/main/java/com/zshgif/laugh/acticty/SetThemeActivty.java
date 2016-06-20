package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;

public class SetThemeActivty extends BaseActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    @ViewInject(R.id.id_toolbar)
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_theme_activty);
        ViewUtils.inject(this);
        preferences = getSharedPreferences("theme",0);
        edit =preferences.edit();
        initToolbar();

    }

    private void initToolbar() {

        mToolbar.setTitle("设置主题颜色");
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

    public void  onClick(View view){
        switch (view.getId()){
            case R.id.blue:
                edit.putInt("theme",1);
                break;
            case R.id.yellow:
                edit.putInt("theme",2);
                break;
            case R.id.pink:
                edit.putInt("theme",3);
                break;
            case R.id.green:
                edit.putInt("theme",4);
                break;
        }
        edit.commit();
        MyActivity.myActivity.finish();
        startActivity(new Intent(this,MyActivity.class));
        finish();
    }
}
