package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.zshgif.laugh.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    public static WelcomeActivity instance;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        timer = new Timer();
        timer.schedule(new RemindTask(), 1500);
        instance =this;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    class RemindTask extends TimerTask {
        public void run() {
            timer.cancel();
            startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(WelcomeActivity.this,MyActivity.class));
    }
}
