package com.ujjwalkumar.qkartseller;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private ImageView imageviewback;
    private LinearLayout linear2;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.about);
        com.google.firebase.FirebaseApp.initializeApp(this);

        imageviewback = findViewById(R.id.imageviewback);
        linear2 = findViewById(R.id.linear2);

        imageviewback.setOnClickListener(_view -> finish());

        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#FFCCBC"));
        gd1.setCornerRadius(30);
        linear2.setBackground(gd1);
    }

}