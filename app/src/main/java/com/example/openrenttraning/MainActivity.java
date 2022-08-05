package com.example.openrenttraning;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.openrenttraning.helper.CheckChange;
import com.example.openrenttraning.helper.getdatetime;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    CheckChange c = new CheckChange();
    FirebaseAuth fa;
    TextView name, email, phone, pass;
    FirebaseFirestore fs;
    CardView strtsession, edit, logout;
    String datee, starttime, endtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        fa = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        edit = findViewById(R.id.edit);
        phone = findViewById(R.id.phone);
        strtsession = findViewById(R.id.strtsession);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        phone.setText(sharedPreferences.getString("phone", " "));
        name.setText(sharedPreferences.getString("name", " "));
        email.setText(sharedPreferences.getString("email", " "));
        pass.setText(sharedPreferences.getString("pass", " "));

        if (sharedPreferences.getBoolean("Running", false)) {
            Intent i = new Intent(MainActivity.this, SessionActivity.class);
            i.putExtra("Email", email.getText().toString());
            startActivity(i);
        }

        String dat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Log.e("dat", String.valueOf(sharedPreferences.getBoolean("Running", false)));
        fs.collection("Questions").document(dat).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot d) {
                if (d.exists()) {
                    starttime = d.getString("Start Time");
                    endtime = d.getString("End Time");
                    datee = dat;
                } else {
                   SharedPreferences.Editor myEdit = sharedPreferences.edit();
                   myEdit.putBoolean("Running", false);
                   myEdit.apply();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });


        strtsession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (starttime == null || endtime == null) {
                    Toast.makeText(MainActivity.this, "Session is not yet created!", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Start Session");

                    final View customLayout = getLayoutInflater().inflate(R.layout.start_session, null);
                    builder.setView(customLayout);
                    AlertDialog dialog = builder.create();
                    CardView confirm = customLayout.findViewById(R.id.confirm);
                    TextView date = customLayout.findViewById(R.id.date);
                    TextView stime = customLayout.findViewById(R.id.stime);
                    TextView etime = customLayout.findViewById(R.id.etime);

                    date.setText(datee);
                    stime.setText(starttime);
                    etime.setText(endtime);

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String t1hour = starttime.substring(0, 2);
                            String t1min = starttime.substring(3, 5);
                            SimpleDateFormat tim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//                          String t1hour = tim.substring(0,2);
//                          String t1min = tim.substring(3,5);
//                          int remahour = Integer.parseInt(t2hour) - Integer.parseInt(t1hour);
//                          int remamin = Integer.parseInt(t2min) - Integer.parseInt(t1min);
                            getdatetime f = new getdatetime(MainActivity.this);
                            f.getdatetime(new getdatetime.VolleyCallBack() {
                                @Override
                                public void onGetdatetime(String Datw) {
                                    try {
                                        String Time = Datw.substring(11,19);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                        Date date1 = simpleDateFormat.parse(starttime+":00");
                                        Date date2 = simpleDateFormat.parse(Time);
                                        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());
                                        long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
                                        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
                                        if(date1.compareTo(date2)<0 || date1.compareTo(date2) == 0){
                                            Intent i = new Intent(MainActivity.this, SessionActivity.class);
                                            i.putExtra("Email", email.getText().toString());
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(MainActivity.this, "Still "+ differenceInHours +" Hour "+ differenceInMinutes +" Minute Left for the Session", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    dialog.show();
                }

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fa.signOut();
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("email", " ");
                myEdit.putString("name", " ");
                myEdit.putString("pass", " ");
                myEdit.putString("phone", " ");
                myEdit.apply();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        if (!CheckPermissions()) {
            RequestPermissions();
        }
    }


    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(MainActivity.this, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        IntentFilter f = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(c, f);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(c);
        super.onStop();
    }
}