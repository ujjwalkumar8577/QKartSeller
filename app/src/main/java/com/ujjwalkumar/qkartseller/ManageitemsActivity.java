package com.ujjwalkumar.qkartseller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageitemsActivity extends AppCompatActivity {

    private final ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference db4 = _firebase.getReference("items");
    private double t = 0;
    private double u = 0;
    private HashMap<String, Object> mp = new HashMap<>();
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> toset = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmpitems = new ArrayList<>();
    private ImageView imageviewback;
    private ListView listview1;
    private EditText edittext1;
    private EditText edittext2;
    private EditText edittext3;
    private EditText edittext4;
    private CheckBox checkbox1;
    private Button buttonclear;
    private Button buttonadd;
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private ChildEventListener _db4_child_listener;
    private SharedPreferences sp1;
    private AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.manageitems);
        com.google.firebase.FirebaseApp.initializeApp(this);

        imageviewback = findViewById(R.id.imageviewback);
        listview1 = findViewById(R.id.listview1);
        edittext1 = findViewById(R.id.edittext1);
        edittext2 = findViewById(R.id.edittext2);
        edittext3 = findViewById(R.id.edittext3);
        edittext4 = findViewById(R.id.edittext4);
        checkbox1 = findViewById(R.id.checkbox1);
        buttonclear = findViewById(R.id.buttonclear);
        buttonadd = findViewById(R.id.buttonadd);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);

        imageviewback.setOnClickListener(_view -> finish());

        checkbox1.setOnCheckedChangeListener((_param1, _param2) -> {
            final boolean _isChecked = _param2;
            if (_isChecked) {
                edittext3.setText(edittext4.getText().toString());
            }
        });

        buttonclear.setOnClickListener(_view -> {
            edittext1.setText("");
            edittext2.setText("");
            edittext3.setText("");
            edittext4.setText("");
            checkbox1.setChecked(false);
        });

        buttonadd.setOnClickListener(_view -> {
            if (!edittext1.getText().toString().equals("")) {
                if (!edittext2.getText().toString().equals("")) {
                    if (!edittext4.getText().toString().equals("")) {
                        if (!edittext3.getText().toString().equals("")) {
                            if (!(Double.parseDouble(edittext3.getText().toString()) > Double.parseDouble(edittext4.getText().toString()))) {
                                mp = new HashMap<>();
                                mp.put("id", db4.push().getKey());
                                mp.put("sellerid", sp1.getString("uid", ""));
                                mp.put("name", edittext1.getText().toString());
                                mp.put("detail", edittext2.getText().toString());
                                mp.put("mrp", edittext4.getText().toString());
                                mp.put("price", edittext3.getText().toString());
                                mp.put("status", "1");
                                db4.child(mp.get("id").toString()).updateChildren(mp);
                                filtered.add(mp);
                                listview1.setAdapter(new Listview1Adapter(filtered));
                                ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                                edittext1.setText("");
                                edittext2.setText("");
                                edittext3.setText("");
                                edittext4.setText("");
                                checkbox1.setChecked(false);
                                edittext1.requestFocus();
                            } else {
                                Toast.makeText(this, "Selling price should be less than MRP", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Enter selling price", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Enter MRP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter item details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT).show();
            }
        });

        _db4_child_listener = new ChildEventListener() {
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
        db4.addChildEventListener(_db4_child_listener);

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

        _loadlist();
    }

    private void _loadlist() {
        db4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                lmpitems = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        lmpitems.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                t = 0;
                u = 0;
                for (int _repeat146 = 0; _repeat146 < lmpitems.size(); _repeat146++) {
                    if (lmpitems.get((int) t).get("sellerid").toString().equals(sp1.getString("uid", ""))) {
                        tmp = lmpitems.get((int) t);
                        filtered.add(tmp);
                    } else {

                    }
                    t++;
                }
                if (filtered.size() > 0) {
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageitemsActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
    }

    private void _delete(final double _pos) {
        db4.child(filtered.get((int) _pos).get("id").toString()).removeValue();
        filtered.remove((int) (_pos));
        listview1.setAdapter(new Listview1Adapter(filtered));
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.manageitm, null);
            }

            final LinearLayout linearout = _v.findViewById(R.id.linearout);
            final LinearLayout linear1 = _v.findViewById(R.id.linear1);
            final LinearLayout linear5 = _v.findViewById(R.id.linear5);
            final LinearLayout linear3 = _v.findViewById(R.id.linear3);
            final TextView textviewitemname = _v.findViewById(R.id.textviewitemname);
            final TextView textviewqty = _v.findViewById(R.id.textviewqty);
            final TextView textview2 = _v.findViewById(R.id.textview2);
            final TextView textviewitemprice = _v.findViewById(R.id.textviewitemprice);
            final TextView textviewitemmrp = _v.findViewById(R.id.textviewitemmrp);
            final Switch switch1 = _v.findViewById(R.id.switch1);
            final ImageView imageviewdeleteitem = _v.findViewById(R.id.imageviewdeleteitem);
            final TextView textviewitemdetail = _v.findViewById(R.id.textviewitemdetail);

            android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
            gd1.setColor(Color.parseColor("#FFCCBC"));
            gd1.setCornerRadius(30);
            linearout.setBackground(gd1);
            textviewqty.setVisibility(View.GONE);
            textviewitemname.setText(filtered.get(_position).get("name").toString());
            textviewitemprice.setText(filtered.get(_position).get("price").toString());
            textviewitemdetail.setText(filtered.get(_position).get("detail").toString());
            textviewitemmrp.setText(filtered.get(_position).get("mrp").toString());
            textviewitemmrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            switch1.setChecked(filtered.get(_position).get("status").toString().equals("1"));
            imageviewdeleteitem.setOnClickListener(_view12 -> {
                confirm.setTitle("Delete");
                confirm.setMessage("Do you want to delete ".concat(filtered.get(_position).get("name").toString().concat(" ?")));
                confirm.setPositiveButton("Yes", (_dialog, _which) -> _delete(_position));
                confirm.setNegativeButton("No", (_dialog, _which) -> {

                });
                confirm.create().show();
            });
            switch1.setOnClickListener(_view1 -> {
                if (filtered.get(_position).get("status").toString().equals("1")) {
                    toset = filtered.get(_position);
                    toset.put("status", "0");
                    switch1.setChecked(false);
                    db4.child(filtered.get(_position).get("id").toString()).updateChildren(toset);
                } else {
                    toset = filtered.get(_position);
                    toset.put("status", "1");
                    switch1.setChecked(true);
                    db4.child(filtered.get(_position).get("id").toString()).updateChildren(toset);
                }
            });

            return _v;
        }
    }

}