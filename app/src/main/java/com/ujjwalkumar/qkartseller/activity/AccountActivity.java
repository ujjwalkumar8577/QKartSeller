package com.ujjwalkumar.qkartseller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ujjwalkumar.qkartseller.R;

public class AccountActivity extends AppCompatActivity {

    private final Intent in = new Intent();
    private LinearLayout linearLayout, linearedit;
    private ImageView imageviewback, imageviewprofile;
    private TextView textviewname, textviewcontact, textviewemail, textviewaddress, textviewsignout;
    private SharedPreferences sp1;
    private AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        linearLayout = findViewById(R.id.linearLayout);
        linearedit = findViewById(R.id.linearedit);
        textviewsignout = findViewById(R.id.textviewsignout);
        imageviewback = findViewById(R.id.imageviewback);
        imageviewprofile = findViewById(R.id.imageviewprofile);
        textviewname = findViewById(R.id.textviewname);
        textviewcontact = findViewById(R.id.textviewcontact);
        textviewemail = findViewById(R.id.textviewemail);
        textviewaddress = findViewById(R.id.textviewaddress);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);

        linearedit.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), EditDetailsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
            finish();
        });

        textviewsignout.setOnClickListener(view -> {
            confirm.setTitle("Sign out");
            confirm.setMessage("Do you want to sign out?");
            confirm.setPositiveButton("Yes", (_dialog, _which) -> {
                FirebaseAuth.getInstance().signOut();
                sp1.edit().putString("uid", "").apply();
                sp1.edit().putString("email", "").apply();
                sp1.edit().putString("name", "").apply();
                sp1.edit().putString("address", "").apply();
                sp1.edit().putString("lat", "").apply();
                sp1.edit().putString("lng", "").apply();
                sp1.edit().putString("contact", "").apply();
                sp1.edit().putString("img", "").apply();
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), AuthenticateActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            });
            confirm.setNegativeButton("No", (_dialog, _which) -> {

            });
            confirm.create().show();
        });

        imageviewback.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
            finish();
        });

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#B0BEC5"));
        gd1.setCornerRadius(180);
        imageviewprofile.setBackground(gd1);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#FFFFFF"));
        gd2.setCornerRadius(50);
        linearLayout.setBackground(gd2);

        textviewname.setText(sp1.getString("name", ""));
        textviewcontact.setText(sp1.getString("contact", ""));
        textviewemail.setText(sp1.getString("email", ""));
        textviewaddress.setText(sp1.getString("address", ""));
        if (!sp1.getString("img", "").equals("")) {
            Glide.with(getApplicationContext()).load(Uri.parse(sp1.getString("img", ""))).into(imageviewprofile);
        }
    }

}