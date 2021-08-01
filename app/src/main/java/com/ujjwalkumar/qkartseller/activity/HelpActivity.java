package com.ujjwalkumar.qkartseller.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ujjwalkumar.qkartseller.R;

import java.util.HashMap;

public class HelpActivity extends AppCompatActivity {

    private HashMap<String, Object> mp = new HashMap<>();

    private ImageView imageviewback;
    private LinearLayout linear2;
    private Button buttonsend;
    private EditText feed;

    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference db5 = firebase.getReference("help");
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.help);

        imageviewback = findViewById(R.id.imageviewback);
        linear2 = findViewById(R.id.linear2);
        buttonsend = findViewById(R.id.buttonsend);
        feed = findViewById(R.id.feed);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(view -> finish());

        buttonsend.setOnClickListener(view -> {
            if (!feed.getText().toString().equals("")) {
                mp = new HashMap<>();
                mp.put("uid", sp1.getString("uid", ""));
                mp.put("email", sp1.getString("email", ""));
                mp.put("oid", getIntent().getStringExtra("oid"));
                mp.put("msg", feed.getText().toString());
                db5.push().updateChildren(mp);
                Toast.makeText(this, "Thank you, we will reach you soon", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please enter the message", Toast.LENGTH_SHORT).show();
            }
        });

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(30);
        buttonsend.setBackground(gd1);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#FFFFFF"));
        gd2.setCornerRadius(30);
        linear2.setBackground(gd2);
    }

}