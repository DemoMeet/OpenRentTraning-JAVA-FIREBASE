package com.example.openrenttraning.admin;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openrenttraning.AnswerActivity;
import com.example.openrenttraning.R;
import com.example.openrenttraning.helper.voicedAdapter;
import com.example.openrenttraning.helper.writtenAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Add_Sessions extends AppCompatActivity {


    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static String mFileName = null;
    private final int PICK_AUDIO = 1;
    String starttime, endtime, datee;
    ImageButton mic, play;
    Uri AudioUri;
    boolean urir = false, doing = false;
    List<String> written = new ArrayList<>();
    List<String> voice = new ArrayList<>();
    List<Boolean> sss = new ArrayList<>();
    List<String> time = new ArrayList<>();

    TextView vq;
    CardView addv, addw, addsession;
    LinearLayout procss;
    RecyclerView l, ll;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sessions);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();

        Intent s = getIntent();
        starttime = s.getStringExtra("StartTime");
        endtime = s.getStringExtra("EndTime");
        datee = s.getStringExtra("Dates");
        l = findViewById(R.id.written);
        ll = findViewById(R.id.voiced);
        addv = findViewById(R.id.addv);
        addw = findViewById(R.id.addw);
        addsession = findViewById(R.id.addsession);
        procss = findViewById(R.id.process);
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

        l.setHasFixedSize(true);
        l.setLayoutManager(new LinearLayoutManager(this));
        writtenAdapter arr = new writtenAdapter(written);
        l.setAdapter(arr);

        ll.setHasFixedSize(true);
        ll.setLayoutManager(new LinearLayoutManager(this));
        voicedAdapter voi = new voicedAdapter(voice, sss,Add_Sessions.this);
        ll.setAdapter(voi);

        fstore.collection("Questions").document(datee).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot d) {
                if (d.exists()) {
                    List ss = (List) d.get("Questions");
                    written.addAll(ss);
                    arr.notifyDataSetChanged();
                }
            }
        });

        StorageReference listRef = storage.getReference().child("Questions/" + "Year " + year + "/Month " + month + "/Date " + day + "/");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    voice.add(uri.toString());
                                    sss.add(true);
                                    voi.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", e.getMessage().toString());
                    }
                });


        addw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_Sessions.this);
                builder.setTitle("Add Questions");

                final View customLayout = getLayoutInflater().inflate(R.layout.written_layout, null);
                builder.setView(customLayout);
                CardView add = customLayout.findViewById(R.id.add);

                AlertDialog dialog = builder.create();
                EditText question = customLayout.findViewById(R.id.wq);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (question.length() >= 0) {
                            written.add(question.getText().toString());
                            arr.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });


        addv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_Sessions.this);
                builder.setTitle("Add Questions");

                final View customLayout = getLayoutInflater().inflate(R.layout.voice_layout, null);
                builder.setView(customLayout);
                ImageButton play = customLayout.findViewById(R.id.play);
                ImageButton mic = customLayout.findViewById(R.id.mic);
                ImageButton stop = customLayout.findViewById(R.id.stop);
                vq = customLayout.findViewById(R.id.vq);
                ImageButton upload = customLayout.findViewById(R.id.upload);
                CardView add = customLayout.findViewById(R.id.add);

                AlertDialog dialog = builder.create();
                mic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!doing){startRecording();}
                    }
                });
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!doing){playAudio();}
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pauseRecording();
                    }
                });
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent audio = new Intent();
                        audio.setType("audio/mpeg");
                        audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        startActivityForResult(Intent.createChooser(audio, "Select Audio"), PICK_AUDIO);
                        urir = true;
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (urir) {
                            voice.add(AudioUri.toString());
                            sss.add(false);
                        } else {
                            voice.add(Uri.fromFile(new File(mFileName)).toString());
                            sss.add(false);
                        }
                        voi.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        addsession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (written.size() == 0 || voice.size() == 0) {
                    Toast.makeText(Add_Sessions.this, "Atleast One Question From Each Is Required!", Toast.LENGTH_LONG).show();
                } else {
                    procss.setVisibility(View.VISIBLE);
                    for (String s : voice) {
                        Uri file = Uri.parse(s);
                        Log.e("AsS",file.getLastPathSegment());
                        String sss = file.getLastPathSegment().replace("/", "_");
                        Log.e("AsS",sss);
                        FirebaseStorage.getInstance().getReference().child("Questions/" + "Year " + year + "/Month " + month + "/Date " + day + "/" + sss).putFile(file).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Error", e.getMessage());
                            }
                        });
                    }
                    int len = voice.size() + written.size();
                    int t2hour = Integer.parseInt(starttime.substring(0,2));
                    int t2min = Integer.parseInt(starttime.substring(3,5));
                    int t3hour = Integer.parseInt(endtime.substring(0,2));
                    int t3min = Integer.parseInt(endtime.substring(3,5));
                    int r1, r2;
                    for(int i =0; i<len; i++){
                        Random random = new Random();
                        r1 = random.nextInt(t3hour + 1 - t2hour) + t2hour;
                        if(r1==t2hour){
                            r2 = random.nextInt(60 - t2min + 1) + t2min;
                        }else{
                            r2 = random.nextInt(60 + 1);
                        }
                        if(r1 == t3hour){
                            r2 = random.nextInt(t3min + 1);
                        }else{
                            r2 = random.nextInt(60 + 1);
                        }
                        if(String.valueOf(r1).length()==1&&String.valueOf(r2).length()==1){
                            time.add("0"+r1+":0"+r2);
                        }else if(String.valueOf(r1).length()==1){
                            time.add("0"+r1+":"+r2);
                        }else if(String.valueOf(r2).length()==1){
                            time.add(r1+":0"+r2);
                        }else{
                            time.add(r1+":"+r2);
                        }
                    }

                    List quelist = new ArrayList(written);
                    Map<String, Object> que = new HashMap<>();

                        que.put("Start Time", starttime);
                        que.put("End Time", endtime);
                        que.put("Questions", quelist);
                        que.put("Times", time);
                        fstore.collection("Questions").document(String.valueOf(datee)).set(que).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                procss.setVisibility(View.GONE);
                                Toast.makeText(Add_Sessions.this, "Questions Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                }
            }
        });
    }

    private void startRecording() {
        doing = true;
        if (CheckPermissions()) {
            urir = false;
            //Log.e("FIle1", mFileName);
            String s = String.valueOf(System.currentTimeMillis()).replaceAll(":", ".");
            //mFileName += "/" + s + ".mp3";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mFileName= Add_Sessions.this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + s + ".mp3";
            }
            else
            {
                mFileName= Environment.getExternalStorageDirectory().toString() + "/" + s + ".mp3";
            }
         //   Random rnd = new Random();
          //  int n = 100000 + rnd.nextInt(900000);
         //   mFileName += "/" + n + ".mp3";
            Log.e("FIle3", mFileName);
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
            vq.setText("Recording Started");
        } else {
            RequestPermissions();
        }
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
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(Add_Sessions.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    public void playAudio() {
        mPlayer = new MediaPlayer();
        doing=true;
        if (urir) {
            try {
                mPlayer.setDataSource(Add_Sessions.this, AudioUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    doing = false;
                }
            });
        } else {
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
                Log.e("TAG", e.getMessage());
            }
        }
    }

    public void pauseRecording() {
        doing = false;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        vq.setText("Recording Stopped");
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO && resultCode == RESULT_OK) {
            AudioUri = data.getData();
            vq.setText("Audio Selected");
        } else {
            urir = false;
        }
    }
}