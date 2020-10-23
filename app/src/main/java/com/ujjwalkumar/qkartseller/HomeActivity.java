package com.ujjwalkumar.qkartseller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.ujjwalkumar.qkartseller.util.RequestNetwork;
import com.ujjwalkumar.qkartseller.util.RequestNetworkController;
import com.ujjwalkumar.qkartseller.util.SketchwareUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double t = 0;
    private String UID = "";
    private double lat = 0;
    private double lng = 0;
    private double u = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private double dist = 0;
    private double lat1 = 0;
    private double lon1 = 0;
    private double lat2 = 0;
    private double lon2 = 0;
    private double dlat = 0;
    private double dlon = 0;
    private double a = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> fmap = new HashMap<>();
    private HashMap<String, Object> order = new HashMap<>();
    private double status = 0;
    private ArrayList<HashMap<String, Object>> lmp_orders = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();

    private ImageView imageviewabout;
    private LinearLayout linearloading;
    private ListView listview1;
    private ImageView imageviewcart;
    private TextView textviewstatus;
    private LinearLayout lineardash;
    private LinearLayout linearcomp;
    private LinearLayout linearitems;
    private LinearLayout linearhelp;
    private LinearLayout linearaccount;

    private Intent inh = new Intent();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private DatabaseReference db2 = _firebase.getReference("consumers");
    private ChildEventListener _db2_child_listener;
    private DatabaseReference db3 = _firebase.getReference("orders");
    private ChildEventListener _db3_child_listener;
    private SharedPreferences sp1;
    private Calendar cal = Calendar.getInstance();
    private AlertDialog.Builder confirm;
    private ObjectAnimator ani1 = new ObjectAnimator();
    private RequestNetwork checkConnection;
    private RequestNetwork.RequestListener _checkConnection_request_listener;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.home);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewabout = (ImageView) findViewById(R.id.imageviewabout);
        linearloading = (LinearLayout) findViewById(R.id.linearloading);
        listview1 = (ListView) findViewById(R.id.listview1);
        imageviewcart = (ImageView) findViewById(R.id.imageviewcart);
        textviewstatus = (TextView) findViewById(R.id.textviewstatus);
        lineardash = (LinearLayout) findViewById(R.id.lineardash);
        linearcomp = (LinearLayout) findViewById(R.id.linearcomp);
        linearitems = (LinearLayout) findViewById(R.id.linearitems);
        linearhelp = (LinearLayout) findViewById(R.id.linearhelp);
        linearaccount = (LinearLayout) findViewById(R.id.linearaccount);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);
        checkConnection = new RequestNetwork(this);

        imageviewabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), AboutActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                tmp = filtered.get((int) _position);
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), OrderdetailsActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                inh.putExtra("map", new Gson().toJson(tmp));
                startActivity(inh);
            }
        });

        lineardash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

            }
        });

        linearcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), MyordersActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        linearitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), ManageitemsActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        linearhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), HelpActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                inh.putExtra("oid", "");
                startActivity(inh);
            }
        });

        linearaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), AccountActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        _db2_child_listener = new ChildEventListener() {
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
        db2.addChildEventListener(_db2_child_listener);

        _db3_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childValue.get("selleruid").toString().equals(UID)) {
                    Notification.Builder mBuilder = new Notification.Builder(HomeActivity.this);
                    mBuilder.setSmallIcon(R.drawable.qkartseller);
                    mBuilder.setContentTitle("Q Kart Seller");
                    mBuilder.setContentText("Received a new order");
                    mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_NO_CREATE);

                    mBuilder.setContentIntent(pendingIntent).setAutoCancel(true);
                    notificationManager.notify(1, mBuilder.build());
                }
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

        _checkConnection_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _response = _param2;

            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;
                SketchwareUtil.showMessage(getApplicationContext(), "No internet connection");
            }
        };

        _auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();

            }
        };
    }

    private void initializeLogic() {
        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        ani1.setTarget(imageviewcart);
        ani1.setPropertyName("translationX");
        ani1.setFloatValues((float) (0), (float) (700));
        ani1.setInterpolator(new BounceInterpolator());
        ani1.setDuration((int) (15000));
        ani1.start();
        checkConnection.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com/", "A", _checkConnection_request_listener);
        _loadlist();
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        db3.addChildEventListener(_db3_child_listener);
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
                for (int _repeat11 = 0; _repeat11 < (int) (lmp_orders.size()); _repeat11++) {
                    if (lmp_orders.get((int) t).get("selleruid").toString().equals(UID) && !lmp_orders.get((int) t).get("status").toString().equals("4")) {
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
                    } else {

                    }
                    t++;
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

            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
            final LinearLayout linear8 = (LinearLayout) _v.findViewById(R.id.linear8);
            final LinearLayout linear9 = (LinearLayout) _v.findViewById(R.id.linear9);
            final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
            final LinearLayout linear10 = (LinearLayout) _v.findViewById(R.id.linear10);
            final TextView textviewname = (TextView) _v.findViewById(R.id.textviewname);
            final TextView textview8 = (TextView) _v.findViewById(R.id.textview8);
            final TextView textviewamt = (TextView) _v.findViewById(R.id.textviewamt);
            final TextView textview9 = (TextView) _v.findViewById(R.id.textview9);
            final TextView textviewdist = (TextView) _v.findViewById(R.id.textviewdist);
            final TextView textview10 = (TextView) _v.findViewById(R.id.textview10);
            final TextView textviewtime = (TextView) _v.findViewById(R.id.textviewtime);
            final LinearLayout linear4 = (LinearLayout) _v.findViewById(R.id.linear4);
            final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
            final LinearLayout linear6 = (LinearLayout) _v.findViewById(R.id.linear6);
            final LinearLayout linear7 = (LinearLayout) _v.findViewById(R.id.linear7);
            final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
            final TextView textview3 = (TextView) _v.findViewById(R.id.textview3);
            final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
            final TextView textview4 = (TextView) _v.findViewById(R.id.textview4);
            final ImageView imageview3 = (ImageView) _v.findViewById(R.id.imageview3);
            final TextView textview5 = (TextView) _v.findViewById(R.id.textview5);
            final ImageView imageview4 = (ImageView) _v.findViewById(R.id.imageview4);
            final TextView textview6 = (TextView) _v.findViewById(R.id.textview6);
            final TextView textview11 = (TextView) _v.findViewById(R.id.textview11);
            final TextView textvieworderid = (TextView) _v.findViewById(R.id.textvieworderid);

            _distance(Double.parseDouble(filtered.get((int) _position).get("lat").toString()), Double.parseDouble(filtered.get((int) _position).get("lng").toString()));
            cal.setTimeInMillis((long) (Double.parseDouble(filtered.get((int) _position).get("time").toString())));
            textviewname.setText(filtered.get((int) _position).get("name").toString());
            textviewamt.setText(filtered.get((int) _position).get("amt").toString());
            textviewdist.setText(new DecimalFormat("0.00").format(dist));
            textviewtime.setText(new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss").format(cal.getTime()));
            textvieworderid.setText(filtered.get((int) _position).get("oid").toString());
            status = Double.parseDouble(filtered.get((int) _position).get("status").toString());
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
            linear4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    confirm.setTitle("Confirm");
                    confirm.setMessage("Do you want to mark it as 'ordered'?");
                    confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            order = new HashMap<>();
                            order.put("oid", filtered.get((int) _position).get("oid").toString());
                            order.put("selleruid", filtered.get((int) _position).get("selleruid").toString());
                            order.put("custuid", filtered.get((int) _position).get("custuid").toString());
                            order.put("amt", filtered.get((int) _position).get("amt").toString());
                            order.put("status", "1");
                            order.put("time", filtered.get((int) _position).get("time").toString());
                            order.put("comment", filtered.get((int) _position).get("comment").toString());
                            order.put("custmap", filtered.get((int) _position).get("custmap").toString());
                            order.put("sellermap", filtered.get((int) _position).get("sellermap").toString());
                            order.put("itemmap", filtered.get((int) _position).get("itemmap").toString());
                            db3.child(order.get("oid").toString()).updateChildren(order);
                            imageview1.setImageResource(R.drawable.status1);
                            imageview2.setImageResource(R.drawable.status0);
                            imageview3.setImageResource(R.drawable.status0);
                            imageview4.setImageResource(R.drawable.status0);
                            filtered.get((int) _position).put("status", "1");
                            SketchwareUtil.showMessage(getApplicationContext(), "Marked as ordered");
                        }
                    });
                    confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {

                        }
                    });
                    confirm.create().show();
                }
            });
            linear5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    confirm.setTitle("Confirm");
                    confirm.setMessage("Do you want to mark it as 'confirmed'?");
                    confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            order = new HashMap<>();
                            order.put("oid", filtered.get((int) _position).get("oid").toString());
                            order.put("selleruid", filtered.get((int) _position).get("selleruid").toString());
                            order.put("custuid", filtered.get((int) _position).get("custuid").toString());
                            order.put("amt", filtered.get((int) _position).get("amt").toString());
                            order.put("status", "2");
                            order.put("time", filtered.get((int) _position).get("time").toString());
                            order.put("comment", filtered.get((int) _position).get("comment").toString());
                            order.put("custmap", filtered.get((int) _position).get("custmap").toString());
                            order.put("sellermap", filtered.get((int) _position).get("sellermap").toString());
                            order.put("itemmap", filtered.get((int) _position).get("itemmap").toString());
                            db3.child(order.get("oid").toString()).updateChildren(order);
                            imageview1.setImageResource(R.drawable.status1);
                            imageview2.setImageResource(R.drawable.status2);
                            imageview3.setImageResource(R.drawable.status0);
                            imageview4.setImageResource(R.drawable.status0);
                            filtered.get((int) _position).put("status", "2");
                            SketchwareUtil.showMessage(getApplicationContext(), "Marked as confirmed");
                        }
                    });
                    confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {

                        }
                    });
                    confirm.create().show();
                }
            });
            linear6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    confirm.setTitle("Confirm");
                    confirm.setMessage("Do you want to mark it as 'shipped'?");
                    confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            order = new HashMap<>();
                            order.put("oid", filtered.get((int) _position).get("oid").toString());
                            order.put("selleruid", filtered.get((int) _position).get("selleruid").toString());
                            order.put("custuid", filtered.get((int) _position).get("custuid").toString());
                            order.put("amt", filtered.get((int) _position).get("amt").toString());
                            order.put("status", "3");
                            order.put("time", filtered.get((int) _position).get("time").toString());
                            order.put("comment", filtered.get((int) _position).get("comment").toString());
                            order.put("custmap", filtered.get((int) _position).get("custmap").toString());
                            order.put("sellermap", filtered.get((int) _position).get("sellermap").toString());
                            order.put("itemmap", filtered.get((int) _position).get("itemmap").toString());
                            db3.child(order.get("oid").toString()).updateChildren(order);
                            imageview1.setImageResource(R.drawable.status1);
                            imageview2.setImageResource(R.drawable.status2);
                            imageview3.setImageResource(R.drawable.status3);
                            imageview4.setImageResource(R.drawable.status0);
                            filtered.get((int) _position).put("status", "3");
                            SketchwareUtil.showMessage(getApplicationContext(), "Marked as shipped");
                        }
                    });
                    confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {

                        }
                    });
                    confirm.create().show();
                }
            });
            linear7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    confirm.setTitle("Confirm");
                    confirm.setMessage("Do you want to mark it as 'delivered'?");
                    confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            order = new HashMap<>();
                            order.put("oid", filtered.get((int) _position).get("oid").toString());
                            order.put("selleruid", filtered.get((int) _position).get("selleruid").toString());
                            order.put("custuid", filtered.get((int) _position).get("custuid").toString());
                            order.put("amt", filtered.get((int) _position).get("amt").toString());
                            order.put("status", "4");
                            order.put("time", filtered.get((int) _position).get("time").toString());
                            order.put("comment", filtered.get((int) _position).get("comment").toString());
                            order.put("custmap", filtered.get((int) _position).get("custmap").toString());
                            order.put("sellermap", filtered.get((int) _position).get("sellermap").toString());
                            order.put("itemmap", filtered.get((int) _position).get("itemmap").toString());
                            db3.child(order.get("oid").toString()).updateChildren(order);
                            imageview1.setImageResource(R.drawable.status1);
                            imageview2.setImageResource(R.drawable.status2);
                            imageview3.setImageResource(R.drawable.status3);
                            imageview4.setImageResource(R.drawable.status4);
                            filtered.get((int) _position).put("status", "4");
                            SketchwareUtil.showMessage(getApplicationContext(), "Marked as delivered");
                        }
                    });
                    confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {

                        }
                    });
                    confirm.create().show();
                }
            });

            return _v;
        }
    }

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }

}
