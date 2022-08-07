package com.example.openrenttraning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openrenttraning.helper.Question;
import com.example.openrenttraning.helper.QuestionModel;
import com.example.openrenttraning.helper.getdatetime;
import com.example.openrenttraning.helper.questionAdapter;
import com.example.openrenttraning.helper.writtenAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;


public class SessionActivity extends AppCompatActivity {

    TextView starttime, endtime, remaining, current, datee;
    FirebaseFirestore fs;
    String strttime, entime, curtime;
    ProgressBar pbar;
    RecyclerView rv;
    questionAdapter arr;
    List<String> written = new ArrayList<>();
    List<String> voice = new ArrayList<>();
    List<String> time = new ArrayList<>();
    getdatetime f;
    ArrayList<QuestionModel> popque = new ArrayList<QuestionModel>();
    ArrayList<QuestionModel> question = new ArrayList<QuestionModel>();
    int ii = 0, iss = 0;
    public static String daatee;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        datee = findViewById(R.id.datee);
        starttime = findViewById(R.id.starttime);
        endtime = findViewById(R.id.endtime);
        remaining = findViewById(R.id.remaining);
        current = findViewById(R.id.current);
        rv = findViewById(R.id.querv);
        pbar = findViewById(R.id.pbar);
        email = getIntent().getStringExtra("Email");
        fs = FirebaseFirestore.getInstance();
        buildRecyclerView();
        f = new getdatetime(SessionActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("Running", true);
        myEdit.apply();
        f.getdatetime(new getdatetime.VolleyCallBack() {
            @Override
            public void onGetdatetime(String Time) {
                String dat = Time.substring(8, 10) + "-" + Time.substring(5, 7) + "-" + Time.substring(0, 4);
                fs.collection("Questions").document(dat).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        if (d.exists()) {
                            strttime = d.getString("Start Time");
                            entime = d.getString("End Time");
                            starttime.setText(strttime);
                            endtime.setText(entime);
                            written = (ArrayList<String>) d.get("Questions");
                            time = (ArrayList<String>) d.get("Times");
                            for (int i = 0; i < written.size(); i++) {
                                popque.add(new QuestionModel(written.get(i), time.get(i) + ":00", false, false));
                            }
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putBoolean("Running", true);
                            myEdit.apply();
                        } else {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putBoolean("Running", false);
                            myEdit.apply();
                            finish();
                        }
                    }
                });

                StorageReference listRef = FirebaseStorage.getInstance().getReference().child("Questions/" + "Year " + Time.substring(0, 4) + "/Month " + Time.substring(5, 7) + "/Date " + Time.substring(8, 10) + "/");
                listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    voice.add(uri.toString());
                                    popque.add(new QuestionModel(voice.get(ii), time.get(ii + written.size() - 1) + ":00", true, false));
                                    ii = ii + 1;
                                }
                            });
                        }

                    }
                });
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                        FirebaseFirestore.getInstance().collection("Answer").whereEqualTo("User", email).whereEqualTo("Date", dat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot qw) {
                                if (!qw.isEmpty()) {
                                    for(iss= 0; iss<popque.size(); iss++){
                                        QuestionModel qq = popque.get(iss);
                                    for(DocumentSnapshot d: qw){
                                        if(d.getString("Question").equals(qq.getQuestion())){
                                            popque.remove(qq);
                                            iss--;
                                        }
                                    }}
                                }
                            }
                        });
                }, 20000);

            }

        });

        blink();

    }

    private void buildRecyclerView() {
        arr = new questionAdapter(question, SessionActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(manager);
        rv.setAdapter(arr);
    }

    void setttime(String time1, String time2) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date1 = simpleDateFormat.parse(time1);
        Date date2 = simpleDateFormat.parse(time2 + ":00");
        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());
        long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

        if (date1.compareTo(date2) < 0 || date1.compareTo(date2) == 0) {
            remaining.setText(differenceInHours + ":" + differenceInMinutes);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("Running", false);
            //clrData();
            myEdit.apply();
            finish();
        }
    }


    void vetttime(String time1) {

        for (int i = 0; i < popque.size(); i++) {
            QuestionModel q = popque.get(i);
            int h1 = Integer.parseInt(time1.substring(0, 2));
            int h2 = Integer.parseInt(q.getPostingtime().substring(0, 2));
            int m1 = Integer.parseInt(time1.substring(3, 5));
            int m2 = Integer.parseInt(q.getPostingtime().substring(3, 5));
            if (h2 - h1 == 0) {
                if (m2 - m1 <= 0) {
                    if (!q.getHas()) {
                        popque.set(i, new QuestionModel(q.getQuestion(), q.getPostingtime(), q.getSss(), true));
                        question.add(new QuestionModel(q.getQuestion(), q.getPostingtime(), q.getSss(), false));
                    }
                }
            } else if (h2 - h1 < 0) {
                if (!q.getHas()) {
                    popque.set(i, new QuestionModel(q.getQuestion(), q.getPostingtime(), q.getSss(), true));
                    question.add(new QuestionModel(q.getQuestion(), q.getPostingtime(), q.getSss(), false));
                }
            }
        }
        arr = new questionAdapter(question, SessionActivity.this);
        rv.setAdapter(arr);
    }

    private void blink() {
        final Handler hander = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        //loadData();
                        f.getdatetime(new getdatetime.VolleyCallBack() {
                            @Override
                            public void onGetdatetime(String Time) {
                                String dat = Time.substring(8, 10) + "-" + Time.substring(5, 7) + "-" + Time.substring(0, 4);
                                datee.setText("For " + dat);
                                daatee = dat;
                                String tim = Time.substring(11, 19);
                                current.setText(tim);
                                curtime = tim;
                                try {
                                    setttime(tim, entime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                vetttime(curtime);

                            }
                        });

                        pbar.setVisibility(View.GONE);
                        blink();
                        //loadData();
                    }
                });
            }
        }).start();
    }
}