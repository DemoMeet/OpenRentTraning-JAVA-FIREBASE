package com.example.openrenttraning.helper;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openrenttraning.AnswerActivity;
import com.example.openrenttraning.R;
import com.example.openrenttraning.SessionActivity;
import com.example.openrenttraning.admin.Add_Sessions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.ViewHolder>{


    Context context;
    ArrayList<QuestionModel>listdata;

    public questionAdapter(ArrayList<QuestionModel>listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.answer_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int p = position;

       if(listdata.get(position).sss){

            holder.wq.setText("Voice Question");
           holder.wq.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent i = new Intent(context, AnswerActivity.class);
                   i.putExtra("Q", "V");
                   i.putExtra("Question", listdata.get(p).getQuestion());
                   i.putExtra("Time", listdata.get(p).getPostingtime());
                   i.putExtra("SSS", listdata.get(p).getSss());
                   context.startActivity(i);
                   ((Activity)context).finish();
               }
           });
        }else{
            holder.wq.setText("Writen Question");
            holder.wq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AnswerActivity.class);
                    i.putExtra("Q", "W");
                    i.putExtra("Question", listdata.get(p).getQuestion());
                    i.putExtra("Time", listdata.get(p).getPostingtime());
                    i.putExtra("SSS", listdata.get(p).getSss());
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llw, llv;
         public TextView wq;

        public ViewHolder(View itemView) {
            super(itemView);
            this.llw =  itemView.findViewById(R.id.llw);
            this.llv =  itemView.findViewById(R.id.llv);
            this.wq =  itemView.findViewById(R.id.wq);
        }
    }
}