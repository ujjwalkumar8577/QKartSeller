package com.ujjwalkumar.qkartseller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

public class HelpActivity extends AppCompatActivity {

    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference db5 = _firebase.getReference("help");
    private HashMap<String, Object> mp = new HashMap<>();
    private ImageView imageviewback;
    private LinearLayout linear2;
    private Button buttonsend;
    private EditText feed;
    private ChildEventListener _db5_child_listener;
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.help);
        com.google.firebase.FirebaseApp.initializeApp(this);

        imageviewback = findViewById(R.id.imageviewback);
        linear2 = findViewById(R.id.linear2);
        buttonsend = findViewById(R.id.buttonsend);
        feed = findViewById(R.id.feed);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(_view -> finish());

        buttonsend.setOnClickListener(_view -> {
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

        _db5_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        db5.addChildEventListener(_db5_child_listener);

        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(30);
        buttonsend.setBackground(gd1);
        android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
        gd2.setColor(Color.parseColor("#FFFFFF"));
        gd2.setCornerRadius(30);
        linear2.setBackground(gd2);
    }

}