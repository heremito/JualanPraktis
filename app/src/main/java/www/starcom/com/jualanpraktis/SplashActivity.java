package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import www.starcom.com.jualanpraktis.Login.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(SplashActivity.this).isLoggedIn()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("tab",0);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    intent.putExtra("tab",0);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
