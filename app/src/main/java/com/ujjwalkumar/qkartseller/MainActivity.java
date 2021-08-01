package com.ujjwalkumar.qkartseller;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final Timer timer = new Timer();
    private final Intent in = new Intent();
    private final ObjectAnimator anix = new ObjectAnimator();
    private final ObjectAnimator aniy = new ObjectAnimator();
    private LinearLayout layout;
    private TimerTask splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        com.google.firebase.FirebaseApp.initializeApp(this);

        layout = findViewById(R.id.layout);

        anix.setTarget(layout);
        anix.setPropertyName("scaleX");
        anix.setFloatValues(0.0f, 1.0f);
        anix.setInterpolator(new DecelerateInterpolator());
        anix.setDuration(500);
        aniy.setTarget(layout);
        aniy.setPropertyName("scaleY");
        aniy.setFloatValues(0.0f, 1.0f);
        aniy.setInterpolator(new DecelerateInterpolator());
        aniy.setDuration(500);
        anix.start();
        aniy.start();

        splash = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    in.setAction(Intent.ACTION_VIEW);
                    in.setClass(getApplicationContext(), AuthenticateActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(in);
                    finish();
                });
            }
        };
        timer.schedule(splash, 500);
    }

}