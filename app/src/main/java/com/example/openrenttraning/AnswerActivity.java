package com.example.openrenttraning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openrenttraning.helper.getdatetime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AnswerActivity extends AppCompatActivity {

    public LinearLayout llw, llv;
    public EditText wa;
    public CardView addwq, addvq;
    public ImageButton qplay, qstop, aplay, astop, amic;
    public TextView wq, vq, va;
    private static String mFileName = null;
    boolean urir = false;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer = new MediaPlayer();
    Boolean doing = false, sss;
    String question, time;
    ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        llw = findViewById(R.id.llw);
        llv = findViewById(R.id.llv);
        wq = findViewById(R.id.wq);
        vq = findViewById(R.id.vq);
        va = findViewById(R.id.va);
        wa = findViewById(R.id.wa);
        addwq = findViewById(R.id.addwq);
        addvq = findViewById(R.id.addvq);
        qplay = findViewById(R.id.qplay);
        qstop = findViewById(R.id.qstop);
        aplay = findViewById(R.id.aplay);
        astop =  findViewById(R.id.astop);
        amic =  findViewById(R.id.amic);
        pbar = findViewById(R.id.pbar);
        question = getIntent().getStringExtra("Question");
        time = getIntent().getStringExtra("Time");
        sss  = getIntent().getBooleanExtra("Sss", false);

        Log.e("TAGs", question);

        if(!getIntent().getStringExtra("Q").equals("W")){
            llw.setVisibility(View.GONE);
            llv.setVisibility(View.VISIBLE);
           qplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!doing){
                        playAudio(question);
                    }
                }
            });
           qstop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vq.setText(pauseRecording());
                }
            });
           amic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!doing){
                        va.setText(startRecording());
                        doing = true;
                    }
                }
            });
           astop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    va.setText(pauseRecording());
                }
            });
            aplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!doing){
                        playAudioI();
                    }
                }
            });
            addvq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!doing){
                        if(mFileName==null){
                            Toast.makeText(AnswerActivity.this, "Record An Answer Then Submit!!", Toast.LENGTH_SHORT).show();
                        }else{
                            pbar.setVisibility(View.VISIBLE);
                            String datee = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                            String year, month, day;
                            if (datee.length() == 9) {
                                day = datee.substring(0, 2);
                                month = "0" + datee.charAt(3);
                                year = datee.substring(5, 9);
                            } else {
                                day = datee.substring(0, 2);
                                month = datee.substring(3, 5);
                                year = datee.substring(6, 10);
                            }
                            Uri file = Uri.fromFile(new File(mFileName));
                            FirebaseStorage.getInstance().getReference().child("Answers/" + "Year " + year + "/Month " + month + "/Date " + day + "/User_" + sharedPreferences.getString("email", " ") + "/" + file.getLastPathSegment()).putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            getdatetime f = new getdatetime(AnswerActivity.this);
                                            f.getdatetime(new getdatetime.VolleyCallBack() {
                                                @Override
                                                public void onGetdatetime(String Time) {
                                                    Map<String, Object> ansmap = new HashMap<>();
                                                    ansmap.put("Answer", String.valueOf(uri));
                                                    ansmap.put("Voice", true);
                                                    ansmap.put("Question", question);
                                                    ansmap.put("Posting Time", time);
                                                    String tim = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                                    ansmap.put("Adding Time", tim);
                                                    ansmap.put("Date", SessionActivity.daatee);
                                                    mFileName = null;
                                                    ansmap.put("User", sharedPreferences.getString("email", " "));
                                                    pbar.setVisibility(View.INVISIBLE);
                                                    FirebaseFirestore.getInstance().collection("Answer").add(ansmap);
                                                    finish();
                                                    startActivity(new Intent(AnswerActivity.this, MainActivity.class));
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
        }else{
            llv.setVisibility(View.GONE);
            llw.setVisibility(View.VISIBLE);
            wq.setText(question);
            addwq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ans = wa.getText().toString();
                    if(ans.length()==0){
                        Toast.makeText(AnswerActivity.this, "Answer Field Cannot Empty!!", Toast.LENGTH_SHORT).show();
                    }else{
                        pbar.setVisibility(View.VISIBLE);
                        getdatetime f = new getdatetime(AnswerActivity.this);
                        f.getdatetime(new getdatetime.VolleyCallBack() {
                            @Override
                            public void onGetdatetime(String Time) {
                                Map<String, Object>ansmap = new HashMap<>();
                                ansmap.put("Answer", ans);
                                ansmap.put("Voice", false);
                                ansmap.put("Question", question);
                                ansmap.put("Posting Time", time);
                                String tim = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                ansmap.put("Adding Time", tim);
                                ansmap.put("Date", SessionActivity.daatee);
                                ansmap.put("User", sharedPreferences.getString("email", " "));
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putBoolean("Running", true);
                                myEdit.apply();
                                pbar.setVisibility(View.INVISIBLE);
                                FirebaseFirestore.getInstance().collection("Answer").add(ansmap);
                                finish();
                                startActivity(new Intent(AnswerActivity.this, MainActivity.class));
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(AnswerActivity.this, MainActivity.class));
    }

    private void playAudioI(){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    doing = false;
                }
            });
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }
    private void playAudio(String audioUrl) {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(audioUrl);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    doing = false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String pauseRecording() {
        doing = false;
        mRecorder.stop();
        mPlayer.stop();
        mRecorder.release();
        mRecorder = null;
        return "Recording Stopped";
    }


    private String startRecording() {
        doing = true;
        urir = false;
        //  mFileName = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator;
        String s = String.valueOf(System.currentTimeMillis()).replaceAll(":", ".");
        //mFileName += "/" + s + ".mp3";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            mFileName= AnswerActivity.this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + s + ".mp3";
        }
        else
        {
            mFileName= Environment.getExternalStorageDirectory().toString() + "/" + s + ".mp3";
        }

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
        mRecorder.start();
        return "Recording Started";
    }

}