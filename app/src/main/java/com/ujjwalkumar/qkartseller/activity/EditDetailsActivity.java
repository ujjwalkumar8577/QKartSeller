package com.ujjwalkumar.qkartseller.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ujjwalkumar.qkartseller.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

public class EditDetailsActivity extends AppCompatActivity {

    public final int REQ_CD_IMAGEPICKER = 101;
    private double lat = 0;
    private double lng = 0;
    private boolean locationSet = false;
    private String downloadURL = "";
    private HashMap<String, Object> mp = new HashMap<>();

    private ImageView imageviewprofile;
    private Button buttondone;
    private LinearLayout linear4, linear21, linear10, linear11, linear22;
    private TextView textviewstatus;
    private MapView mapview1;
    private EditText edittextname, edittextcontact, edittextaddress1, edittextaddress2, edittextaddress3, edittextaddress4, edittextrange;

    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final FirebaseStorage firebase_storage = FirebaseStorage.getInstance();
    private final Intent in = new Intent();
    private final DatabaseReference db1 = firebase.getReference("sellers");
    private final Intent imagePicker = new Intent(Intent.ACTION_GET_CONTENT);
    private SharedPreferences sp1;

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

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        imagePicker.setType("image/*");
        imagePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        edittextname.setText(sp1.getString("name", ""));
        edittextcontact.setText(sp1.getString("contact", ""));
        edittextaddress1.setText(sp1.getString("address", ""));
        edittextrange.setText(sp1.getString("range", ""));
        downloadURL = sp1.getString("img", "");
        if (!sp1.getString("img", "").equals("")) {
            Glide.with(getApplicationContext()).load(Uri.parse(sp1.getString("img", ""))).into(imageviewprofile);
        }

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(50);
        buttondone.setBackground(gd1);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#CFD8DC"));
        gd2.setCornerRadius(150);
        imageviewprofile.setBackground(gd2);

        GradientDrawable gd3 = new GradientDrawable();
        gd3.setColor(Color.parseColor("#FFCCBC"));
        gd3.setCornerRadius(80);
        linear21.setBackground(gd3);
        linear10.setBackground(gd3);
        linear11.setBackground(gd3);
        linear22.setBackground(gd3);

        GradientDrawable gd4 = new GradientDrawable();
        gd4.setColor(Color.parseColor("#FFFFFF"));
        gd4.setCornerRadius(50);
        linear4.setBackground(gd4);

        imageviewprofile.setOnClickListener(view -> startActivityForResult(imagePicker, REQ_CD_IMAGEPICKER));

        buttondone.setOnClickListener(view -> {
            if (!edittextname.getText().toString().equals("")) {
                if (!edittextcontact.getText().toString().equals("")) {
                    if (!edittextaddress1.getText().toString().equals("")) {
                        if (!edittextrange.getText().toString().equals("")) {
                            if (locationSet) {
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
                                in.setAction(Intent.ACTION_VIEW);
                                in.setClass(getApplicationContext(), HomeActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(in);
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

        mapview1.getMapAsync(googleMap -> {
            if (ActivityCompat.checkSelfPermission(EditDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                return;
            }

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(25.0, 81.0)));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(18));

            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location currentLocation = googleMap.getMyLocation();
                    lat = currentLocation.getLatitude();
                    lng = currentLocation.getLongitude();
                    locationSet = true;
                    textviewstatus.setText("Location updated");
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    return false;
                }
            });
        });
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

        if (requestCode == REQ_CD_IMAGEPICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            Uri filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                imageviewprofile.setImageBitmap(bitmap);
                uploadImage(filePath);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview1.onPause();
    }

    @Override
    public void onResume() {
        mapview1.onResume();
        super.onResume();
    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            // showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image ...");
            progressDialog.show();

            // uploading file and adding listeners on upload or failure of image
            String filename = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference stref = firebase_storage.getReference("sellers").child(filename);
            stref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditDetailsActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                    })

                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditDetailsActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    })

                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    })

                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                stref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadURL = uri.toString();
                                    }
                                });
                            }
                        }
                    });
        }
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

}