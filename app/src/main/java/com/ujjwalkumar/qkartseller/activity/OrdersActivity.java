package com.ujjwalkumar.qkartseller.activity;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkartseller.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrdersActivity extends AppCompatActivity {

    private final ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
    private double t = 0;
    private double u = 0;
    private String UID = "";
    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private double sum = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private HashMap<String, Object> fmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmp_orders = new ArrayList<>();

    private ImageView imageviewback;
    private TextView textviewstatus, textviewamt;
    private ListView listview1;

    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final Intent inmo = new Intent();
    private final DatabaseReference db3 = _firebase.getReference("orders");
    private final Calendar cal = Calendar.getInstance();
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        imageviewback = findViewById(R.id.imageviewback);
        textviewstatus = findViewById(R.id.textviewstatus);
        listview1 = findViewById(R.id.listview1);
        textviewamt = findViewById(R.id.textviewamt);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(view -> finish());

        listview1.setOnItemClickListener((_param1, _param2, _param3, _param4) -> {
            tmp = filtered.get(_param3);
            inmo.setAction(Intent.ACTION_VIEW);
            inmo.setClass(getApplicationContext(), OrderDetailsActivity.class);
            inmo.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            inmo.putExtra("map", new Gson().toJson(tmp));
            startActivity(inmo);
        });

        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        loadlist();
    }

    private void loadlist() {
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lmp_orders = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> _map = data.getValue(_ind);
                        lmp_orders.add(_map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                t = 0;
                u = 0;
                sum = 0;
                for (int i = 0; i < lmp_orders.size(); i++) {
                    if (lmp_orders.get((int) t).get("selleruid").toString().equals(UID) && lmp_orders.get((int) t).get("status").toString().equals("4")) {
                        cmap = new Gson().fromJson(lmp_orders.get((int) t).get("custmap").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private double calculateDistance(final double latc, final double lngc) {
        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lng);
        double lat2 = Math.toRadians(latc);
        double lon2 = Math.toRadians(lngc);
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + ((Math.cos(lat1 / 2) * Math.cos(lat2 / 2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2)));
        double dist = 12742 * Math.asin(Math.sqrt(a));
        return dist;
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
                v = inflater.inflate(R.layout.adapterorders, null);
            }

            final TextView textviewname = v.findViewById(R.id.textviewname);
            final TextView textviewamt = v.findViewById(R.id.textviewamt);
            final TextView textviewdist = v.findViewById(R.id.textviewdist);
            final TextView textviewtime = v.findViewById(R.id.textviewtime);
            final ImageView imageview1 = v.findViewById(R.id.imageview1);
            final ImageView imageview2 = v.findViewById(R.id.imageview2);
            final ImageView imageview3 = v.findViewById(R.id.imageview3);
            final ImageView imageview4 = v.findViewById(R.id.imageview4);
            final TextView textvieworderid = v.findViewById(R.id.textvieworderid);

            double dist = calculateDistance(Double.parseDouble(filtered.get(position).get("lat").toString()), Double.parseDouble(filtered.get(position).get("lng").toString()));
            cal.setTimeInMillis((long) (Double.parseDouble(filtered.get(position).get("time").toString())));
            textviewname.setText(filtered.get(position).get("name").toString());
            textviewamt.setText(filtered.get(position).get("amt").toString());
            textviewdist.setText(new DecimalFormat("0.00").format(dist));
            textviewtime.setText(new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss").format(cal.getTime()));
            textvieworderid.setText(filtered.get(position).get("oid").toString());
            imageview1.setImageResource(R.drawable.status1);
            imageview2.setImageResource(R.drawable.status2);
            imageview3.setImageResource(R.drawable.status3);
            imageview4.setImageResource(R.drawable.status4);

            return v;
        }
    }

}