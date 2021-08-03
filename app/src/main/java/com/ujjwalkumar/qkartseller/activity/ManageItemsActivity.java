package com.ujjwalkumar.qkartseller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ujjwalkumar.qkartseller.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageItemsActivity extends AppCompatActivity {

    private HashMap<String, Object> mp = new HashMap<>();
    private HashMap<String, Object> toset = new HashMap<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();

    private ImageView imageviewback;
    private ListView listview1;
    private EditText edittext1, edittext2, edittext3, edittext4;
    private CheckBox checkbox1;
    private Button buttonclear, buttonadd;

    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference db4 = firebase.getReference("items");
    private SharedPreferences sp1;
    private AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageitems);

        imageviewback = findViewById(R.id.imageviewback);
        listview1 = findViewById(R.id.listview1);
        edittext1 = findViewById(R.id.edittext1);
        edittext2 = findViewById(R.id.edittext2);
        edittext3 = findViewById(R.id.edittext3);
        edittext4 = findViewById(R.id.edittext4);
        checkbox1 = findViewById(R.id.checkbox1);
        buttonclear = findViewById(R.id.buttonclear);
        buttonadd = findViewById(R.id.buttonadd);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);

        imageviewback.setOnClickListener(view -> finish());

        checkbox1.setOnCheckedChangeListener((param1, param2) -> {
            if (param2) {
                edittext3.setText(edittext4.getText().toString());
            }
        });

        buttonclear.setOnClickListener(view -> {
            edittext1.setText("");
            edittext2.setText("");
            edittext3.setText("");
            edittext4.setText("");
            checkbox1.setChecked(false);
        });

        buttonadd.setOnClickListener(view -> {
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

        loadlist();
    }

    private void loadlist() {
        db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filtered = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        if (map.get("sellerid").toString().equals(sp1.getString("uid", ""))) {
                            filtered.add(map);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (filtered.size() > 0) {
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageItemsActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        ArrayList<HashMap<String, Object>> data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> arr) {
            data = arr;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int index) {
            return data.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = view;
            if (v == null) {
                v = inflater.inflate(R.layout.adaptermanageitems, null);
            }

            final LinearLayout linearout = v.findViewById(R.id.linearout);
            final TextView textviewitemname = v.findViewById(R.id.textviewitemname);
            final TextView textviewqty = v.findViewById(R.id.textviewqty);
            final TextView textviewitemprice = v.findViewById(R.id.textviewitemprice);
            final TextView textviewitemmrp = v.findViewById(R.id.textviewitemmrp);
            final Switch switch1 = v.findViewById(R.id.switch1);
            final ImageView imageviewdeleteitem = v.findViewById(R.id.imageviewdeleteitem);
            final TextView textviewitemdetail = v.findViewById(R.id.textviewitemdetail);

            GradientDrawable gd1 = new GradientDrawable();
            gd1.setColor(Color.parseColor("#FFCCBC"));
            gd1.setCornerRadius(30);
            linearout.setBackground(gd1);

            textviewqty.setVisibility(View.GONE);
            textviewitemname.setText(filtered.get(position).get("name").toString());
            textviewitemprice.setText(filtered.get(position).get("price").toString());
            textviewitemdetail.setText(filtered.get(position).get("detail").toString());
            textviewitemmrp.setText(filtered.get(position).get("mrp").toString());
            textviewitemmrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            switch1.setChecked(filtered.get(position).get("status").toString().equals("1"));

            imageviewdeleteitem.setOnClickListener(view12 -> {
                confirm.setTitle("Delete");
                confirm.setMessage("Do you want to delete ".concat(filtered.get(position).get("name").toString().concat(" ?")));
                confirm.setPositiveButton("Yes", (_dialog, _which) -> _delete(position));
                confirm.setNegativeButton("No", (_dialog, _which) -> {

                });
                confirm.create().show();
            });
            switch1.setOnClickListener(view1 -> {
                if (filtered.get(position).get("status").toString().equals("1")) {
                    toset = filtered.get(position);
                    toset.put("status", "0");
                    switch1.setChecked(false);
                    db4.child(filtered.get(position).get("id").toString()).updateChildren(toset);
                } else {
                    toset = filtered.get(position);
                    toset.put("status", "1");
                    switch1.setChecked(true);
                    db4.child(filtered.get(position).get("id").toString()).updateChildren(toset);
                }
            });

            return v;
        }
    }

}