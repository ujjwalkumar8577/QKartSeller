package com.ujjwalkumar.qkartseller.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkartseller.R;
import com.ujjwalkumar.qkartseller.utility.GoogleMapController;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {

    private final Intent ino = new Intent();
    private HashMap<String, Object> tmp = new HashMap<>();
    private double lat = 0;
    private double lng = 0;
    private ArrayList<HashMap<String, Object>> item = new ArrayList<>();

    private TextView textviewname, textviewaddress, textviewreview, textviewamt;
    private ImageView imageviewback, imageviewcall, imageviewlocate, imageview1, imageview2, imageview3, imageview4;
    private MapView mapview1;
    private GoogleMapController mapview1_controller;
    private LinearLayout linear11, linear12, linear13;
    private ListView listview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetails);

        imageviewback = findViewById(R.id.imageviewback);
        textviewname = findViewById(R.id.textviewname);
        textviewaddress = findViewById(R.id.textviewaddress);
        imageviewcall = findViewById(R.id.imageviewcall);
        imageviewlocate = findViewById(R.id.imageviewlocate);
        mapview1 = findViewById(R.id.mapview1);
        mapview1.onCreate(savedInstanceState);
        linear11 = findViewById(R.id.linear11);
        linear12 = findViewById(R.id.linear12);
        linear13 = findViewById(R.id.linear13);
        imageview1 = findViewById(R.id.imageview1);
        imageview2 = findViewById(R.id.imageview2);
        imageview3 = findViewById(R.id.imageview3);
        imageview4 = findViewById(R.id.imageview4);
        listview1 = findViewById(R.id.listview1);
        textviewreview = findViewById(R.id.textviewreview);
        textviewamt = findViewById(R.id.textviewamt);

        imageviewback.setOnClickListener(view -> finish());

        imageviewcall.setOnClickListener(view -> {
            ino.setAction(Intent.ACTION_CALL);
            ino.setData(Uri.parse("tel:".concat(tmp.get("contact").toString())));
            startActivity(ino);
        });

        imageviewlocate.setOnClickListener(view -> {
            ino.setAction(Intent.ACTION_VIEW);
            ino.setData(Uri.parse("google.navigation:q=".concat(tmp.get("lat").toString().concat(",".concat(tmp.get("lng").toString())))));
            startActivity(ino);
        });

        mapview1_controller = new GoogleMapController(mapview1, _googleMap -> {
            mapview1_controller.setGoogleMap(_googleMap);
            mapview1_controller.moveCamera(lat, lng);
            mapview1_controller.zoomTo(15);
            mapview1_controller.addMarker("id", lat, lng);
            mapview1_controller.setMarkerIcon("id", R.drawable.ic_location_on_black);
        });

        linear11.setOnClickListener(view -> {

        });

        linear12.setOnClickListener(view -> {

        });

        linear13.setOnClickListener(view -> {

        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1000);
        } else {
            initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    private void initializeLogic() {
        tmp = new Gson().fromJson(getIntent().getStringExtra("map"), new TypeToken<HashMap<String, Object>>() {}.getType());
        item = new Gson().fromJson(tmp.get("itemmap").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType());
        lat = Double.parseDouble(tmp.get("lat").toString());
        lng = Double.parseDouble(tmp.get("lng").toString());
        textviewname.setText(tmp.get("name").toString());
        textviewaddress.setText(tmp.get("address").toString());
        textviewamt.setText(tmp.get("amt").toString());
        if (tmp.get("comment").toString().equals("")) {
            textviewreview.setText("Not reviewed yet");
        } else {
            textviewreview.setText(tmp.get("comment").toString());
        }
        if ((Double.parseDouble(tmp.get("status").toString()) > 1) || (Double.parseDouble(tmp.get("status").toString()) == 1)) {
            imageview1.setImageResource(R.drawable.status1);
        }
        if ((Double.parseDouble(tmp.get("status").toString()) > 2) || (Double.parseDouble(tmp.get("status").toString()) == 2)) {
            imageview2.setImageResource(R.drawable.status2);
        }
        if ((Double.parseDouble(tmp.get("status").toString()) > 3) || (Double.parseDouble(tmp.get("status").toString()) == 3)) {
            imageview3.setImageResource(R.drawable.status3);
        }
        if ((Double.parseDouble(tmp.get("status").toString()) > 4) || (Double.parseDouble(tmp.get("status").toString()) == 4)) {
            imageview4.setImageResource(R.drawable.status4);
        }
        listview1.setAdapter(new Listview1Adapter(item));
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview1.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapview1.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview1.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview1.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapview1.onStop();
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
        public View getView(final int _position, View view, ViewGroup viewGroup) {
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

            imageviewdeleteitem.setVisibility(View.GONE);
            switch1.setVisibility(View.GONE);
            textviewitemmrp.setVisibility(View.INVISIBLE);
            textviewitemname.setText(item.get(_position).get("name").toString());
            textviewitemprice.setText(item.get(_position).get("price").toString());
            textviewitemdetail.setText(item.get(_position).get("detail").toString());
            textviewqty.setText(item.get(_position).get("qty").toString());

            return v;
        }
    }

}