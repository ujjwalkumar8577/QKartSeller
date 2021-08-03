package com.ujjwalkumar.qkartseller.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.qkartseller.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        imageViewBack = findViewById(R.id.imageViewBack);
        linearLayout = findViewById(R.id.linearLayout);

        imageViewBack.setOnClickListener(view -> finish());

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFCCBC"));
        gd1.setCornerRadius(30);
        linearLayout.setBackground(gd1);
    }

}