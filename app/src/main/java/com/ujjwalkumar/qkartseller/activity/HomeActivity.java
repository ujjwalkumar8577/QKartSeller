package com.ujjwalkumar.qkartseller.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkartseller.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private String UID = "";
    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> order = new HashMap<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();

    private ImageView imageviewabout, imageviewcart;
    private ListView listview1;
    private TextView textviewstatus;
    private LinearLayout lineardash, linearcomp, linearitems, linearhelp, linearaccount, linearloading;

    private final Intent in = new Intent();
    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference db2 = firebase.getReference("consumers");
    private final DatabaseReference db3 = firebase.getReference("orders");
    private final Calendar cal = Calendar.getInstance();
    private final ObjectAnimator ani1 = new ObjectAnimator();
    private SharedPreferences sp1;
    private AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        imageviewabout = findViewById(R.id.imageviewabout);
        linearloading = findViewById(R.id.linearloading);
        listview1 = findViewById(R.id.listview1);
        imageviewcart = findViewById(R.id.imageviewcart);
        textviewstatus = findViewById(R.id.textviewstatus);
        lineardash = findViewById(R.id.lineardash);
        linearcomp = findViewById(R.id.linearcomp);
        linearitems = findViewById(R.id.linearitems);
        linearhelp = findViewById(R.id.linearhelp);
        linearaccount = findViewById(R.id.linearaccount);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);
        if(!checkConnectivity())
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

        imageviewabout.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), AboutActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
        });

        listview1.setOnItemClickListener((param1, param2, param3, param4) -> {
            tmp = filtered.get(param3);
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), OrderDetailsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            in.putExtra("map", new Gson().toJson(tmp));
            startActivity(in);
        });

        lineardash.setOnClickListener(view -> {

        });

        linearcomp.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), OrdersActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
        });

        linearitems.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), ManageItemsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
        });

        linearhelp.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), HelpActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            in.putExtra("oid", "");
            startActivity(in);
        });

        linearaccount.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), AccountActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
        });

        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        ani1.setTarget(imageviewcart);
        ani1.setPropertyName("translationX");
        ani1.setFloatValues((float) (0), (float) (700));
        ani1.setInterpolator(new BounceInterpolator());
        ani1.setDuration(15000);
        ani1.start();
        loadlist();
    }

    private void loadlist() {
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filtered = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        if (map.get("selleruid").toString().equals(UID) && !map.get("status").toString().equals("4")) {
                            cmap = new Gson().fromJson(map.get("custmap").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
                            map.put("custid", cmap.get("custid").toString());
                            map.put("name", cmap.get("name").toString());
                            map.put("address", cmap.get("address").toString());
                            map.put("lat", cmap.get("lat").toString());
                            map.put("lng", cmap.get("lng").toString());
                            map.put("contact", cmap.get("contact").toString());
                            filtered.add(map);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (filtered.size() > 0) {
                    ani1.cancel();
                    linearloading.setVisibility(View.GONE);
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("No pending orders found");
                    ani1.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.isAvailable();
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
            final LinearLayout linear4 = v.findViewById(R.id.linear4);
            final LinearLayout linear5 = v.findViewById(R.id.linear5);
            final LinearLayout linear6 = v.findViewById(R.id.linear6);
            final LinearLayout linear7 = v.findViewById(R.id.linear7);
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
            int status = Integer.parseInt(filtered.get(position).get("status").toString());
            if ((status > 1) || (status == 1)) {
                imageview1.setImageResource(R.drawable.status1);
            }
            if ((status > 2) || (status == 2)) {
                imageview2.setImageResource(R.drawable.status2);
            }
            if ((status > 3) || (status == 3)) {
                imageview3.setImageResource(R.drawable.status3);
            }
            if ((status > 4) || (status == 4)) {
                imageview4.setImageResource(R.drawable.status4);
            }
            linear4.setOnClickListener(view14 -> {
                confirm.setTitle("Confirm");
                confirm.setMessage("Do you want to mark it as 'ordered'?");
                confirm.setPositiveButton("Yes", (dialog, _which) -> {
                    order = new HashMap<>();
                    order.put("oid", filtered.get(position).get("oid").toString());
                    order.put("selleruid", filtered.get(position).get("selleruid").toString());
                    order.put("custuid", filtered.get(position).get("custuid").toString());
                    order.put("amt", filtered.get(position).get("amt").toString());
                    order.put("status", "1");
                    order.put("time", filtered.get(position).get("time").toString());
                    order.put("comment", filtered.get(position).get("comment").toString());
                    order.put("custmap", filtered.get(position).get("custmap").toString());
                    order.put("sellermap", filtered.get(position).get("sellermap").toString());
                    order.put("itemmap", filtered.get(position).get("itemmap").toString());
                    db3.child(order.get("oid").toString()).updateChildren(order);
                    imageview1.setImageResource(R.drawable.status1);
                    imageview2.setImageResource(R.drawable.status0);
                    imageview3.setImageResource(R.drawable.status0);
                    imageview4.setImageResource(R.drawable.status0);
                    filtered.get(position).put("status", "1");
                    Toast.makeText(HomeActivity.this, "Marked as ordered", Toast.LENGTH_SHORT).show();
                });
                confirm.setNegativeButton("No", (dialog, _which) -> {

                });
                confirm.create().show();
            });
            linear5.setOnClickListener(view13 -> {
                confirm.setTitle("Confirm");
                confirm.setMessage("Do you want to mark it as 'confirmed'?");
                confirm.setPositiveButton("Yes", (dialog, _which) -> {
                    order = new HashMap<>();
                    order.put("oid", filtered.get(position).get("oid").toString());
                    order.put("selleruid", filtered.get(position).get("selleruid").toString());
                    order.put("custuid", filtered.get(position).get("custuid").toString());
                    order.put("amt", filtered.get(position).get("amt").toString());
                    order.put("status", "2");
                    order.put("time", filtered.get(position).get("time").toString());
                    order.put("comment", filtered.get(position).get("comment").toString());
                    order.put("custmap", filtered.get(position).get("custmap").toString());
                    order.put("sellermap", filtered.get(position).get("sellermap").toString());
                    order.put("itemmap", filtered.get(position).get("itemmap").toString());
                    db3.child(order.get("oid").toString()).updateChildren(order);
                    imageview1.setImageResource(R.drawable.status1);
                    imageview2.setImageResource(R.drawable.status2);
                    imageview3.setImageResource(R.drawable.status0);
                    imageview4.setImageResource(R.drawable.status0);
                    filtered.get(position).put("status", "2");
                    Toast.makeText(HomeActivity.this, "Marked as confirmed", Toast.LENGTH_SHORT).show();
                });
                confirm.setNegativeButton("No", (dialog, _which) -> {

                });
                confirm.create().show();
            });
            linear6.setOnClickListener(view12 -> {
                confirm.setTitle("Confirm");
                confirm.setMessage("Do you want to mark it as 'shipped'?");
                confirm.setPositiveButton("Yes", (dialog, _which) -> {
                    order = new HashMap<>();
                    order.put("oid", filtered.get(position).get("oid").toString());
                    order.put("selleruid", filtered.get(position).get("selleruid").toString());
                    order.put("custuid", filtered.get(position).get("custuid").toString());
                    order.put("amt", filtered.get(position).get("amt").toString());
                    order.put("status", "3");
                    order.put("time", filtered.get(position).get("time").toString());
                    order.put("comment", filtered.get(position).get("comment").toString());
                    order.put("custmap", filtered.get(position).get("custmap").toString());
                    order.put("sellermap", filtered.get(position).get("sellermap").toString());
                    order.put("itemmap", filtered.get(position).get("itemmap").toString());
                    db3.child(order.get("oid").toString()).updateChildren(order);
                    imageview1.setImageResource(R.drawable.status1);
                    imageview2.setImageResource(R.drawable.status2);
                    imageview3.setImageResource(R.drawable.status3);
                    imageview4.setImageResource(R.drawable.status0);
                    filtered.get(position).put("status", "3");
                    Toast.makeText(HomeActivity.this, "Marked as shipped", Toast.LENGTH_SHORT).show();
                });
                confirm.setNegativeButton("No", (dialog, _which) -> {

                });
                confirm.create().show();
            });
            linear7.setOnClickListener(view1 -> {
                confirm.setTitle("Confirm");
                confirm.setMessage("Do you want to mark it as 'delivered'?");
                confirm.setPositiveButton("Yes", (dialog, _which) -> {
                    order = new HashMap<>();
                    order.put("oid", filtered.get(position).get("oid").toString());
                    order.put("selleruid", filtered.get(position).get("selleruid").toString());
                    order.put("custuid", filtered.get(position).get("custuid").toString());
                    order.put("amt", filtered.get(position).get("amt").toString());
                    order.put("status", "4");
                    order.put("time", filtered.get(position).get("time").toString());
                    order.put("comment", filtered.get(position).get("comment").toString());
                    order.put("custmap", filtered.get(position).get("custmap").toString());
                    order.put("sellermap", filtered.get(position).get("sellermap").toString());
                    order.put("itemmap", filtered.get(position).get("itemmap").toString());
                    db3.child(order.get("oid").toString()).updateChildren(order);
                    imageview1.setImageResource(R.drawable.status1);
                    imageview2.setImageResource(R.drawable.status2);
                    imageview3.setImageResource(R.drawable.status3);
                    imageview4.setImageResource(R.drawable.status4);
                    filtered.get(position).put("status", "4");
                    Toast.makeText(HomeActivity.this, "Marked as delivered", Toast.LENGTH_SHORT).show();
                });
                confirm.setNegativeButton("No", (dialog, _which) -> {

                });
                confirm.create().show();
            });

            return v;
        }
    }

}