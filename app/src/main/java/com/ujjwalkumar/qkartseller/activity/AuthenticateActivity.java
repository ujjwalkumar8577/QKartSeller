package com.ujjwalkumar.qkartseller.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ujjwalkumar.qkartseller.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticateActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> lmp = new ArrayList<>();

    private LinearLayout linearlogin, linearsignup, linear14, linear19, linear20, linear4, linear10, linear11;
    private TextView textviewforgot, textviewsignup, textviewlogin;
    private EditText edittextle, edittextlp, edittextse, edittextsp;
    private ImageView imageviewlogin, imageviewsignup;

    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final Intent in = new Intent();
    private final ObjectAnimator anix = new ObjectAnimator();
    private final ObjectAnimator aniy = new ObjectAnimator();
    private final DatabaseReference db1 = firebase.getReference("sellers");
    private final ObjectAnimator ani = new ObjectAnimator();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> auth_create_user_listener;
    private OnCompleteListener<AuthResult> auth_sign_in_listener;
    private OnCompleteListener<Void> auth_reset_password_listener;
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.authenticate);

        linearlogin = findViewById(R.id.linearlogin);
        linearsignup = findViewById(R.id.linearsignup);
        linear14 = findViewById(R.id.linear14);
        textviewforgot = findViewById(R.id.textviewforgot);
        linear19 = findViewById(R.id.linear19);
        linear20 = findViewById(R.id.linear20);
        edittextle = findViewById(R.id.edittextle);
        edittextlp = findViewById(R.id.edittextlp);
        imageviewlogin = findViewById(R.id.imageviewlogin);
        textviewsignup = findViewById(R.id.textviewsignup);
        linear4 = findViewById(R.id.linear4);
        linear10 = findViewById(R.id.linear10);
        linear11 = findViewById(R.id.linear11);
        edittextse = findViewById(R.id.edittextse);
        edittextsp = findViewById(R.id.edittextsp);
        imageviewsignup = findViewById(R.id.imageviewsignup);
        textviewlogin = findViewById(R.id.textviewlogin);

        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
            if (sp1.getString("name", "").equals("")) {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), EditDetailsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            } else {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            }
        }

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(50);
        linear14.setBackground(gd1);
        linear4.setBackground(gd1);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#FFCCBC"));
        gd2.setCornerRadius(80);
        linear19.setBackground(gd2);
        linear20.setBackground(gd2);
        linear10.setBackground(gd2);
        linear11.setBackground(gd2);

        linearlogin.setVisibility(View.VISIBLE);
        linearsignup.setVisibility(View.GONE);

        textviewforgot.setOnClickListener(view -> {
            if (edittextle.getText().toString().equals("")) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            } else {
                auth.sendPasswordResetEmail(edittextle.getText().toString()).addOnCompleteListener(auth_reset_password_listener);
            }
        });

        imageviewlogin.setOnClickListener(view -> {
            if (!edittextle.getText().toString().equals("")) {
                if (!edittextlp.getText().toString().equals("")) {
                    imageviewlogin.setImageResource(R.drawable.ic_rotate_right_black);
                    ani.setTarget(imageviewlogin);
                    ani.setPropertyName("rotation");
                    ani.setFloatValues((float) (0), (float) (720));
                    ani.setDuration(5000);
                    ani.setInterpolator(new LinearInterpolator());
                    ani.start();
                    sp1.edit().putString("email", edittextle.getText().toString()).apply();
                    sp1.edit().putString("password", edittextlp.getText().toString()).apply();
                    auth.signInWithEmailAndPassword(edittextle.getText().toString(), edittextlp.getText().toString()).addOnCompleteListener(AuthenticateActivity.this, auth_sign_in_listener);
                } else {
                    Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            }
        });

        textviewsignup.setOnClickListener(view -> {
            linearsignup.setVisibility(View.VISIBLE);
            linearlogin.setAlpha(1);
            linearsignup.setAlpha(0);
            anix.setTarget(linearsignup);
            anix.setPropertyName("alpha");
            anix.setFloatValues(0, 1);
            anix.setDuration(500);
            aniy.setTarget(linearlogin);
            aniy.setPropertyName("alpha");
            aniy.setFloatValues(1, 0);
            aniy.setDuration(500);
            anix.start();
            aniy.start();
        });

        imageviewsignup.setOnClickListener(view -> {
            if (!edittextse.getText().toString().equals("")) {
                if (!edittextsp.getText().toString().equals("")) {
                    imageviewsignup.setImageResource(R.drawable.ic_rotate_right_black);
                    ani.setTarget(imageviewsignup);
                    ani.setPropertyName("rotation");
                    ani.setFloatValues((float) (0), (float) (720));
                    ani.setDuration(5000);
                    ani.setInterpolator(new LinearInterpolator());
                    ani.start();
                    sp1.edit().putString("email", edittextse.getText().toString()).apply();
                    sp1.edit().putString("password", edittextsp.getText().toString()).apply();
                    auth.createUserWithEmailAndPassword(edittextse.getText().toString(), edittextsp.getText().toString()).addOnCompleteListener(AuthenticateActivity.this, auth_create_user_listener);
                } else {
                    Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            }
        });

        textviewlogin.setOnClickListener(view -> {
            linearlogin.setVisibility(View.VISIBLE);
            linearsignup.setAlpha((float) (1));
            linearlogin.setAlpha((float) (0));
            anix.setTarget(linearlogin);
            anix.setPropertyName("alpha");
            anix.setFloatValues((float) (0), (float) (1));
            anix.setDuration(500);
            aniy.setTarget(linearsignup);
            aniy.setPropertyName("alpha");
            aniy.setFloatValues((float) (1), (float) (0));
            aniy.setDuration(500);
            anix.start();
            aniy.start();
        });

        auth_create_user_listener = param1 -> {
            final boolean success = param1.isSuccessful();
            final String errorMessage = param1.getException() != null ? param1.getException().getMessage() : "";
            ani.cancel();
            imageviewsignup.setRotation((float) (0));
            imageviewsignup.setImageResource(R.drawable.ic_arrow_forward_black);
            if (success) {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), EditDetailsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        };

        auth_sign_in_listener = param1 -> {
            final boolean success = param1.isSuccessful();
            final String errorMessage = param1.getException() != null ? param1.getException().getMessage() : "";
            if (success) {
                db1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lmp = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot data : snapshot.getChildren()) {
                                HashMap<String, Object> map = data.getValue(ind);
                                lmp.add(map);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        int flag = -1;
                        for (int i = 0; i < lmp.size(); i++) {
                            if (lmp.get(i).get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                flag = i;
                                break;
                            }
                        }
                        if (!(flag == -1)) {
                            sp1.edit().putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                            sp1.edit().putString("email", lmp.get(flag).get("email").toString()).apply();
                            sp1.edit().putString("name", lmp.get(flag).get("name").toString()).apply();
                            sp1.edit().putString("address", lmp.get(flag).get("address").toString()).apply();
                            sp1.edit().putString("lat", lmp.get(flag).get("lat").toString()).apply();
                            sp1.edit().putString("lng", lmp.get(flag).get("lng").toString()).apply();
                            sp1.edit().putString("contact", lmp.get(flag).get("contact").toString()).apply();
                            sp1.edit().putString("img", lmp.get(flag).get("img").toString()).apply();
                            in.setAction(Intent.ACTION_VIEW);
                            in.setClass(getApplicationContext(), HomeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(in);
                            finish();
                        } else {
                            Toast.makeText(AuthenticateActivity.this, "This email corresponds to a customer.", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
            ani.cancel();
            imageviewlogin.setRotation((float) (0));
            imageviewlogin.setImageResource(R.drawable.ic_arrow_forward_black);
        };

        auth_reset_password_listener = param1 -> {
            final boolean success = param1.isSuccessful();
            if (success) {
                Toast.makeText(this, "Password reset mail sent.", Toast.LENGTH_SHORT).show();
            }
        };

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lmp = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        lmp.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}