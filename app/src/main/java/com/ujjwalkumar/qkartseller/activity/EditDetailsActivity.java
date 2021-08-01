package com.ujjwalkumar.qkartseller.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ujjwalkumar.qkartseller.R;
import com.ujjwalkumar.qkartseller.utility.FileUtil;
import com.ujjwalkumar.qkartseller.utility.GoogleMapController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class EditDetailsActivity extends AppCompatActivity {

    public final int REQ_CD_IMAGEPICKER = 101;
    private final Timer timer = new Timer();
    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage firebase_storage = FirebaseStorage.getInstance();
    private final Intent ine = new Intent();
    private final DatabaseReference db1 = firebase.getReference("sellers");
    private final Intent imagePicker = new Intent(Intent.ACTION_GET_CONTENT);
    private final StorageReference fbstorage = firebase_storage.getReference("users");
    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> mp = new HashMap<>();
    private boolean mapReady = false;
    private String downloadURL = "";
    private String path = "";

    private ImageView imageviewprofile;
    private Button buttondone;
    private LinearLayout linear4, linear21, linear10, linear11, linear22;
    private TextView textviewstatus;
    private MapView mapview1;
    private GoogleMapController mapview1_controller;
    private EditText edittextname, edittextcontact, edittextaddress1, edittextaddress2, edittextaddress3, edittextaddress4, edittextrange;
    private LocationManager locate;
    private LocationListener locate_location_listener;
    private SharedPreferences sp1;
    private TimerTask wait;
    private OnCompleteListener<Uri> fbstorage_upload_success_listener;
    private OnProgressListener fbstorage_upload_progress_listener;
    private OnFailureListener fbstorage_failure_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdetails);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        imageviewprofile = findViewById(R.id.imageviewprofile);
        buttondone = findViewById(R.id.buttondone);
        linear4 = findViewById(R.id.linear4);
        linear21 = findViewById(R.id.linear21);
        linear10 = findViewById(R.id.linear10);
        linear11 = findViewById(R.id.linear11);
        linear22 = findViewById(R.id.linear22);
        textviewstatus = findViewById(R.id.textviewstatus);
        mapview1 = findViewById(R.id.mapview1);
        mapview1.onCreate(savedInstanceState);
        edittextname = findViewById(R.id.edittextname);
        edittextcontact = findViewById(R.id.edittextcontact);
        edittextaddress1 = findViewById(R.id.edittextaddress1);
        edittextaddress2 = findViewById(R.id.edittextaddress2);
        edittextaddress3 = findViewById(R.id.edittextaddress3);
        edittextaddress4 = findViewById(R.id.edittextaddress4);
        edittextrange = findViewById(R.id.edittextrange);
        locate = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        imagePicker.setType("image/*");
        imagePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        imageviewprofile.setOnClickListener(view -> startActivityForResult(imagePicker, REQ_CD_IMAGEPICKER));

        buttondone.setOnClickListener(view -> {
            if (!edittextname.getText().toString().equals("")) {
                if (!edittextcontact.getText().toString().equals("")) {
                    if (!edittextaddress1.getText().toString().equals("")) {
                        if (!edittextrange.getText().toString().equals("")) {
                            if (!(lat == 0)) {
                                saveInfo();
                                mp = new HashMap<>();
                                mp.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mp.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                mp.put("name", sp1.getString("name", ""));
                                mp.put("address", sp1.getString("address", ""));
                                mp.put("contact", sp1.getString("contact", ""));
                                mp.put("range", sp1.getString("range", ""));
                                mp.put("img", sp1.getString("img", ""));
                                mp.put("lat", sp1.getString("lat", ""));
                                mp.put("lng", sp1.getString("lng", ""));
                                db1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(mp);
                                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                                ine.setAction(Intent.ACTION_VIEW);
                                ine.setClass(getApplicationContext(), HomeActivity.class);
                                ine.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(ine);
                                finish();
                            } else {
                                Toast.makeText(this, "Location not updated", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Enter delivery range", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter contact", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            }
        });

        mapview1_controller = new GoogleMapController(mapview1, _googleMap -> {
            mapview1_controller.setGoogleMap(_googleMap);
            mapReady = true;
        });

        locate_location_listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location param1) {
                final double _lat = param1.getLatitude();
                final double _lng = param1.getLongitude();
                lat = _lat;
                lng = _lng;
                textviewstatus.setText("Location updated");
                locate.removeUpdates(locate_location_listener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        fbstorage_upload_success_listener = param1 -> {
            downloadURL = param1.getResult().toString();
            imageviewprofile.setAlpha((float) (1));
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
        };

        fbstorage_failure_listener = param1 -> {
            final String _message = param1.getMessage();
            Toast.makeText(this, "Image not uploaded", Toast.LENGTH_SHORT).show();
        };

        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(50);
        buttondone.setBackground(gd1);
        android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
        gd2.setColor(Color.parseColor("#CFD8DC"));
        gd2.setCornerRadius(150);
        imageviewprofile.setBackground(gd2);
        android.graphics.drawable.GradientDrawable gd4 = new android.graphics.drawable.GradientDrawable();
        gd4.setColor(Color.parseColor("#FFFFFF"));
        gd4.setCornerRadius(50);
        linear4.setBackground(gd4);
        android.graphics.drawable.GradientDrawable gd3 = new android.graphics.drawable.GradientDrawable();
        gd3.setColor(Color.parseColor("#FFCCBC"));
        gd3.setCornerRadius(80);
        linear21.setBackground(gd3);
        android.graphics.drawable.GradientDrawable gd6 = new android.graphics.drawable.GradientDrawable();
        gd6.setColor(Color.parseColor("#FFCCBC"));
        gd6.setCornerRadius(80);
        linear10.setBackground(gd6);
        android.graphics.drawable.GradientDrawable gd7 = new android.graphics.drawable.GradientDrawable();
        gd7.setColor(Color.parseColor("#FFCCBC"));
        gd7.setCornerRadius(80);
        linear11.setBackground(gd7);
        android.graphics.drawable.GradientDrawable gd8 = new android.graphics.drawable.GradientDrawable();
        gd8.setColor(Color.parseColor("#FFCCBC"));
        gd8.setCornerRadius(80);
        linear22.setBackground(gd8);
        mapReady = false;
        if (ContextCompat.checkSelfPermission(EditDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locate.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locate_location_listener);
        }
        try {
            android.location.Criteria criteria = new android.location.Criteria();
            String bestProvider = locate.getBestProvider(criteria, true);
            Location location = locate.getLastKnownLocation(bestProvider);
            if (location == null) {
                Toast.makeText(this, "Please check yoour GPS", Toast.LENGTH_SHORT).show();
            }
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                textviewstatus.setText("Location updated");
                setLocation(lat, lng);
                locate.removeUpdates(locate_location_listener);
            }
        } catch (Exception e) {
        }
        edittextname.setText(sp1.getString("name", ""));
        edittextcontact.setText(sp1.getString("contact", ""));
        edittextaddress1.setText(sp1.getString("address", ""));
        edittextrange.setText(sp1.getString("range", ""));
        downloadURL = sp1.getString("img", "");
        if (!sp1.getString("img", "").equals("")) {
            Glide.with(getApplicationContext()).load(Uri.parse(sp1.getString("img", ""))).into(imageviewprofile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CD_IMAGEPICKER && resultCode==Activity.RESULT_OK) {
            ArrayList<String> filePath = new ArrayList<>();
            if (data != null) {
                if (data.getClipData() != null) {
                    for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                        ClipData.Item item = data.getClipData().getItemAt(index);
                        filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), item.getUri()));
                    }
                } else {
                    filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), data.getData()));
                }
            }
            path = filePath.get(0);
            imageviewprofile.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(path, 1024, 1024));
            imageviewprofile.setAlpha(0.5f);
            fbstorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid().concat(Uri.parse(path).getLastPathSegment())).putFile(Uri.fromFile(new File(path))).addOnFailureListener(fbstorage_failure_listener).addOnProgressListener(fbstorage_upload_progress_listener).continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> fbstorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid().concat(Uri.parse(path).getLastPathSegment())).getDownloadUrl()).addOnCompleteListener(fbstorage_upload_success_listener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locate.removeUpdates(locate_location_listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(EditDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locate.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locate_location_listener);
        }
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
    public void onStop() {
        super.onStop();
        mapview1.onStop();
    }

    private void saveInfo() {
        sp1.edit().putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
        sp1.edit().putString("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).apply();
        sp1.edit().putString("name", edittextname.getText().toString()).apply();
        sp1.edit().putString("address", edittextaddress1.getText().toString().concat(" , ".concat(edittextaddress2.getText().toString().concat(" \n".concat(edittextaddress3.getText().toString().concat(" \n".concat(edittextaddress4.getText().toString()))))))).apply();
        sp1.edit().putString("contact", edittextcontact.getText().toString()).apply();
        sp1.edit().putString("range", edittextrange.getText().toString()).apply();
        sp1.edit().putString("img", downloadURL).apply();
        sp1.edit().putString("lat", String.valueOf(lat)).apply();
        sp1.edit().putString("lng", String.valueOf(lng)).apply();
    }

    private void setLocation(final double latitude, final double longitude) {
        if (mapReady) {
            mapview1_controller.moveCamera(latitude, longitude);
            mapview1_controller.zoomTo(15);
            mapview1_controller.addMarker("id", latitude, longitude);
            mapview1_controller.setMarkerIcon("id", R.drawable.ic_location_on_black);
            mapview1_controller.setMarkerVisible("id", true);
        } else {
            wait = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> setLocation(latitude, longitude));
                }
            };
            timer.schedule(wait, 1000);
        }
    }

}