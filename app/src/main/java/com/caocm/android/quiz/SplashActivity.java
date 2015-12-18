package com.caocm.android.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by caocm_000 on 12/17/2015.
 */
public class SplashActivity extends Activity{

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_splash);
        float currentVersion = 1.0f;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        float dbversion = preferences.getFloat("DBVERSION", 0);
        //if(dbversion < currentVersion) {
        DBUpdateIntentService.startActionDBUpdate(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        }, 5000);

    }

}
