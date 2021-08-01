package com.ujjwalkumar.qkartseller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.ujjwalkumar.qkartseller.util.GoogleMapController;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderdetailsActivity extends AppCompatActivity {

    private final Intent ino = new Intent();
    private HashMap<String, Object> tmp = new HashMap<>();
    private double lat = 0;
    private double lng = 0;
    private ArrayList<HashMap<String, Object>> item = new ArrayList<>();
    private ImageView imageviewback;
    private TextView textviewname;
    private TextView textviewaddress;
    private ImageView imageviewcall;
    private ImageView imageviewlocate;
    private MapView mapview1;
    private GoogleMapController _mapview1_controller;
    private LinearLayout linear11;
    private LinearLayout linear12;
    private LinearLayout linear13;
    private ImageView imageview1;
    private ImageView imageview2;
    private ImageView imageview3;
    private ImageView imageview4;
    private ListView listview1;
    private TextView textviewreview;
    private TextView textviewamt;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.orderdetails);
        com.google.firebase.FirebaseApp.initializeApp(this);

        imageviewback = findViewById(R.id.imageviewback);
        textviewname = findViewById(R.id.textviewname);
        textviewaddress = findViewById(R.id.textviewaddress);
        imageviewcall = findViewById(R.id.imageviewcall);
        imageviewlocate = findViewById(R.id.imageviewlocate);
        mapview1 = findViewById(R.id.mapview1);
        mapview1.onCreate(_savedInstanceState);
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

        imageviewback.setOnClickListener(_view -> finish());

        imageviewcall.setOnClickListener(_view -> {
            ino.setAction(Intent.ACTION_CALL);
            ino.setData(Uri.parse("tel:".concat(tmp.get("contact").toString())));
            startActivity(ino);
        });

        imageviewlocate.setOnClickListener(_view -> {
            ino.setAction(Intent.ACTION_VIEW);
            ino.setData(Uri.parse("google.navigation:q=".concat(tmp.get("lat").toString().concat(",".concat(tmp.get("lng").toString())))));
            startActivity(ino);
        });

        _mapview1_controller = new GoogleMapController(mapview1, _googleMap -> {
            _mapview1_controller.setGoogleMap(_googleMap);
            _mapview1_controller.moveCamera(lat, lng);
            _mapview1_controller.zoomTo(15);
            _mapview1_controller.addMarker("id", lat, lng);
            _mapview1_controller.setMarkerIcon("id", R.drawable.ic_location_on_black);
        });

        linear11.setOnClickListener(_view -> {

        });

        linear12.setOnClickListener(_view -> {

        });

        linear13.setOnClickListener(_view -> {

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
        tmp = new Gson().fromJson(getIntent().getStringExtra("map"), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        item = new Gson().fromJson(tmp.get("itemmap").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
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
            imageviewdeleteitem.setVisibility(View.GONE);
            switch1.setVisibility(View.GONE);
            textviewitemmrp.setVisibility(View.INVISIBLE);
            textviewitemname.setText(item.get(_position).get("name").toString());
            textviewitemprice.setText(item.get(_position).get("price").toString());
            textviewitemdetail.setText(item.get(_position).get("detail").toString());
            textviewqty.setText(item.get(_position).get("qty").toString());

            return _v;
        }
    }

}