package com.ujjwalkumar.qkartseller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyordersActivity extends AppCompatActivity {

    private final ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final Intent inmo = new Intent();
    private final DatabaseReference db3 = _firebase.getReference("orders");
    private final Calendar cal = Calendar.getInstance();
    private double t = 0;
    private double u = 0;
    private String UID = "";
    private double lat1 = 0;
    private double lat = 0;
    private double lon1 = 0;
    private double lng = 0;
    private double lat2 = 0;
    private double lon2 = 0;
    private double dlat = 0;
    private double dlon = 0;
    private double a = 0;
    private double dist = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private double sum = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private HashMap<String, Object> fmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmp_orders = new ArrayList<>();
    private ImageView imageviewback;
    private TextView textviewstatus;
    private ListView listview1;
    private TextView textviewamt;
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private ChildEventListener _db3_child_listener;
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.myorders);
        com.google.firebase.FirebaseApp.initializeApp(this);

        imageviewback = findViewById(R.id.imageviewback);
        textviewstatus = findViewById(R.id.textviewstatus);
        listview1 = findViewById(R.id.listview1);
        textviewamt = findViewById(R.id.textviewamt);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(_view -> finish());

        listview1.setOnItemClickListener((_param1, _param2, _param3, _param4) -> {
            final int _position = _param3;
            tmp = filtered.get(_position);
            inmo.setAction(Intent.ACTION_VIEW);
            inmo.setClass(getApplicationContext(), OrderdetailsActivity.class);
            inmo.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            inmo.putExtra("map", new Gson().toJson(tmp));
            startActivity(inmo);
        });

        _db3_child_listener = new ChildEventListener() {
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
        db3.addChildEventListener(_db3_child_listener);

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

        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        _loadlist();
    }

    private void _loadlist() {
        db3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                lmp_orders = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        lmp_orders.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                t = 0;
                u = 0;
                sum = 0;
                for (int _repeat13 = 0; _repeat13 < lmp_orders.size(); _repeat13++) {
                    if (lmp_orders.get((int) t).get("selleruid").toString().equals(UID) && lmp_orders.get((int) t).get("status").toString().equals("4")) {
                        cmap = new Gson().fromJson(lmp_orders.get((int) t).get("custmap").toString(), new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                        fmap = lmp_orders.get((int) t);
                        fmap.put("custid", cmap.get("custid").toString());
                        fmap.put("name", cmap.get("name").toString());
                        fmap.put("address", cmap.get("address").toString());
                        fmap.put("lat", cmap.get("lat").toString());
                        fmap.put("lng", cmap.get("lng").toString());
                        fmap.put("contact", cmap.get("contact").toString());
                        filtered.add(fmap);
                        sum = sum + Double.parseDouble(lmp_orders.get((int) t).get("amt").toString());
                        u++;
                    } else {

                    }
                    t++;
                }
                if (filtered.size() > 0) {
                    textviewstatus.setVisibility(View.GONE);
                    textviewamt.setText(String.valueOf(sum));
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("No orders found");
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
    }

    private void _distance(final double _latc, final double _lngc) {
        lat1 = Math.toRadians(lat);
        lon1 = Math.toRadians(lng);
        lat2 = Math.toRadians(_latc);
        lon2 = Math.toRadians(_lngc);
        dlat = lat2 - lat1;
        dlon = lon2 - lon1;
        a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + ((Math.cos(lat1 / 2) * Math.cos(lat2 / 2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2)));
        dist = 12742 * Math.asin(Math.sqrt(a));
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
                _v = _inflater.inflate(R.layout.orders, null);
            }

            final LinearLayout linear1 = _v.findViewById(R.id.linear1);
            final LinearLayout linear2 = _v.findViewById(R.id.linear2);
            final LinearLayout linear8 = _v.findViewById(R.id.linear8);
            final LinearLayout linear9 = _v.findViewById(R.id.linear9);
            final LinearLayout linear3 = _v.findViewById(R.id.linear3);
            final LinearLayout linear10 = _v.findViewById(R.id.linear10);
            final TextView textviewname = _v.findViewById(R.id.textviewname);
            final TextView textview8 = _v.findViewById(R.id.textview8);
            final TextView textviewamt = _v.findViewById(R.id.textviewamt);
            final TextView textview9 = _v.findViewById(R.id.textview9);
            final TextView textviewdist = _v.findViewById(R.id.textviewdist);
            final TextView textview10 = _v.findViewById(R.id.textview10);
            final TextView textviewtime = _v.findViewById(R.id.textviewtime);
            final LinearLayout linear4 = _v.findViewById(R.id.linear4);
            final LinearLayout linear5 = _v.findViewById(R.id.linear5);
            final LinearLayout linear6 = _v.findViewById(R.id.linear6);
            final LinearLayout linear7 = _v.findViewById(R.id.linear7);
            final ImageView imageview1 = _v.findViewById(R.id.imageview1);
            final TextView textview3 = _v.findViewById(R.id.textview3);
            final ImageView imageview2 = _v.findViewById(R.id.imageview2);
            final TextView textview4 = _v.findViewById(R.id.textview4);
            final ImageView imageview3 = _v.findViewById(R.id.imageview3);
            final TextView textview5 = _v.findViewById(R.id.textview5);
            final ImageView imageview4 = _v.findViewById(R.id.imageview4);
            final TextView textview6 = _v.findViewById(R.id.textview6);
            final TextView textview11 = _v.findViewById(R.id.textview11);
            final TextView textvieworderid = _v.findViewById(R.id.textvieworderid);

            _distance(Double.parseDouble(filtered.get(_position).get("lat").toString()), Double.parseDouble(filtered.get(_position).get("lng").toString()));
            cal.setTimeInMillis((long) (Double.parseDouble(filtered.get(_position).get("time").toString())));
            textviewname.setText(filtered.get(_position).get("name").toString());
            textviewamt.setText(filtered.get(_position).get("amt").toString());
            textviewdist.setText(new DecimalFormat("0.00").format(dist));
            textviewtime.setText(new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss").format(cal.getTime()));
            textvieworderid.setText(filtered.get(_position).get("oid").toString());
            imageview1.setImageResource(R.drawable.status1);
            imageview2.setImageResource(R.drawable.status2);
            imageview3.setImageResource(R.drawable.status3);
            imageview4.setImageResource(R.drawable.status4);

            return _v;
        }
    }

}