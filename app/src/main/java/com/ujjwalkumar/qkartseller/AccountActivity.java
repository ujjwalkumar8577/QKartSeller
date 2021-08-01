package com.ujjwalkumar.qkartseller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private final Intent inp = new Intent();
    private LinearLayout linear4;
    private LinearLayout linearedit;
    private TextView textviewsignout;
    private ImageView imageviewback;
    private ImageView imageviewprofile;
    private TextView textviewname;
    private TextView textviewcontact;
    private TextView textviewemail;
    private TextView textviewaddress;
    private SharedPreferences sp1;
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.account);
        com.google.firebase.FirebaseApp.initializeApp(this);

        linear4 = findViewById(R.id.linear4);
        linearedit = findViewById(R.id.linearedit);
        textviewsignout = findViewById(R.id.textviewsignout);
        imageviewback = findViewById(R.id.imageviewback);
        imageviewprofile = findViewById(R.id.imageviewprofile);
        textviewname = findViewById(R.id.textviewname);
        textviewcontact = findViewById(R.id.textviewcontact);
        textviewemail = findViewById(R.id.textviewemail);
        textviewaddress = findViewById(R.id.textviewaddress);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        confirm = new AlertDialog.Builder(this);

        linearedit.setOnClickListener(_view -> {
            inp.setAction(Intent.ACTION_VIEW);
            inp.setClass(getApplicationContext(), EditdetailsActivity.class);
            inp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inp);
            finish();
        });

        textviewsignout.setOnClickListener(_view -> {
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
                inp.setAction(Intent.ACTION_VIEW);
                inp.setClass(getApplicationContext(), AuthenticateActivity.class);
                inp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inp);
                finish();
            });
            confirm.setNegativeButton("No", (_dialog, _which) -> {

            });
            confirm.create().show();
        });

        imageviewback.setOnClickListener(_view -> {
            inp.setAction(Intent.ACTION_VIEW);
            inp.setClass(getApplicationContext(), HomeActivity.class);
            inp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inp);
            finish();
        });

        _auth_create_user_listener = _param1 -> {
            final boolean _success = _param1.isSuccessful();
            final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

        };

        _auth_sign_in_listener = _param1 -> {
            final boolean _success = _param1.isSuccessful();
            final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

        };

        _auth_reset_password_listener = _param1 -> {
            final boolean _success = _param1.isSuccessful();

        };

        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#B0BEC5"));
        gd1.setCornerRadius(180);
        imageviewprofile.setBackground(gd1);
        android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
        gd2.setColor(Color.parseColor("#FFFFFF"));
        gd2.setCornerRadius(50);
        linear4.setBackground(gd2);
        textviewname.setText(sp1.getString("name", ""));
        textviewcontact.setText(sp1.getString("contact", ""));
        textviewemail.setText(sp1.getString("email", ""));
        textviewaddress.setText(sp1.getString("address", ""));
        if (!sp1.getString("img", "").equals("")) {
            Glide.with(getApplicationContext()).load(Uri.parse(sp1.getString("img", ""))).into(imageviewprofile);
        }
    }

}