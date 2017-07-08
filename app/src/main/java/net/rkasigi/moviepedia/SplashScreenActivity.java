/*
 * This project was born to complete one of the terms of graduation from
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 *
 * July 8, 2017
 */
package net.rkasigi.moviepedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * SplashScreenActivity
 *
 * @author Rendi Kasigi
 * @version 1.0
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Intent i = new Intent(this, MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();

            }
        }, 2000);
    }
}
