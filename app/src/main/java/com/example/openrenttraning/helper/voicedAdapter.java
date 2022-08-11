package com.example.openrenttraning.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.openrenttraning.MainActivity;
import com.example.openrenttraning.R;
import com.example.openrenttraning.admin.Add_Sessions;

import java.io.IOException;
import java.util.List;

public class voicedAdapter extends RecyclerView.Adapter<voicedAdapter.ViewHolder> {

    List<String> listdata;
    List<Boolean> sss;
    private MediaPlayer mPlayer = new MediaPlayer();
    boolean play = false;
    Context context;

    public voicedAdapter(List<String> listdata, List<Boolean> sss, Context context) {
        this.listdata = listdata;
        this.context = context;
        this.sss = sss;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_voice_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int p) {
        int position = p;
        holder.serial.setText(String.valueOf(position + 1) + ". ");
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!play) {
                    play = true;
                    if (sss.get(position)) {
                        playAudioI(listdata.get(position));
                    } else {
                        playAudio(listdata.get(position));
                    }
                }
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listdata.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    private void playAudioI(String audioUrl) {

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(audioUrl);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    play = false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playAudio(String mFileName) {
        try {
            mPlayer.setDataSource(context, Uri.parse(mFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton imageView, play;
        public TextView serial;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.delete);
            this.play = itemView.findViewById(R.id.play);
            this.serial = itemView.findViewById(R.id.serial);
        }
    }
}