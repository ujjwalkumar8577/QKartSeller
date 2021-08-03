package com.ujjwalkumar.qkartseller.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.qkartseller.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;

    private final Timer timer = new Timer();
    private final Intent in = new Intent();
    private final ObjectAnimator anix = new ObjectAnimator();
    private final ObjectAnimator aniy = new ObjectAnimator();
    private TimerTask splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        com.google.firebase.FirebaseApp.initializeApp(this);

        layout = findViewById(R.id.layout);

        anix.setTarget(layout);
        anix.setPropertyName("scaleX");
        anix.setFloatValues(0, 1);
        anix.setInterpolator(new DecelerateInterpolator());
        anix.setDuration(500);
        aniy.setTarget(layout);
        aniy.setPropertyName("scaleY");
        aniy.setFloatValues(0, 1);
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