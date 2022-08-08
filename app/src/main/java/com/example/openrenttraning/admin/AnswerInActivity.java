package com.example.openrenttraning.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.AudioAttributes;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.openrenttraning.AnswerActivity;
import com.example.openrenttraning.MainActivity;
import com.example.openrenttraning.R;
import com.example.openrenttraning.SessionActivity;
import com.example.openrenttraning.helper.getdatetime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AnswerInActivity extends AppCompatActivity {

    public LinearLayout llw, llv;
    public ImageButton qplay, aplay;
    public TextView wq, vq, va, wa, at, pt, dt, user;
    private MediaPlayer mPlayer = new MediaPlayer();
    Boolean doing = false, sss;
    String question, answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_in);
        llw = findViewById(R.id.llw);
        llv = findViewById(R.id.llv);
        wq = findViewById(R.id.wq);
        vq = findViewById(R.id.vq);
        va = findViewById(R.id.va);
        wa = findViewById(R.id.wa);
        qplay = findViewById(R.id.qplay);
        at = findViewById(R.id.atime);
        pt = findViewById(R.id.ptime);
        dt = findViewById(R.id.dtime);
        user = findViewById(R.id.email);
        aplay = findViewById(R.id.aplay);
        question = getIntent().getStringExtra("Question");
        answer = getIntent().getStringExtra("Answer");
        at.setText(getIntent().getStringExtra("aTime"));
        pt.setText(getIntent().getStringExtra("pTime"));
        dt.setText(getIntent().getStringExtra("dTime"));
        user.setText(AnswerViewActivity.user);
        sss = getIntent().getBooleanExtra("Sss", false);


        if (!getIntent().getStringExtra("Q").equals("W")) {
            llw.setVisibility(View.GONE);
            llv.setVisibility(View.VISIBLE);
            qplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!doing) {
                        playAudio(question);
                    }
                }
            });
            aplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!doing) {
                        playAudio(answer);
                    }
                }
            });

        } else {
            llv.setVisibility(View.GONE);
            llw.setVisibility(View.VISIBLE);
            wq.setText(question);
            wa.setText(answer);
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
}