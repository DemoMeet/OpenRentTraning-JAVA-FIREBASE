package com.example.openrenttraning.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.openrenttraning.AnswerActivity;
import com.example.openrenttraning.R;
import com.example.openrenttraning.admin.AnswerInActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class answerViewAdapter extends RecyclerView.Adapter<answerViewAdapter.ViewHolder> {


    Context context;
    ArrayList<AnswerModel> listdata;
    public answerViewAdapter(ArrayList<AnswerModel> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.answer_layout1, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int p = position;
        holder.ptime.setText(listdata.get(position).postingtime +":00");
        holder.atime.setText(listdata.get(position).addingtime);
        try {
            holder.dtime.setText(setttime(listdata.get(position).postingtime +":00", listdata.get(position).addingtime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(listdata.get(position).sss){
            holder.type.setText("Voice Question");
            holder.llout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AnswerInActivity.class);
                    i.putExtra("Q", "V");
                    i.putExtra("Question", listdata.get(p).getQuestion());
                    i.putExtra("pTime", listdata.get(p).getPostingtime());
                    i.putExtra("aTime", listdata.get(p).getAddingtime());
                    i.putExtra("Answer", listdata.get(p).getAnswer());
                    i.putExtra("dTime", holder.dtime.getText().toString());
                    i.putExtra("SSS", listdata.get(p).getSss());
                    context.startActivity(i);
                }
            });
        }else{
            holder.type.setText("Writen Question");
            holder.llout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AnswerInActivity.class);
                    i.putExtra("Q", "W");
                    i.putExtra("Question", listdata.get(p).getQuestion());
                    i.putExtra("pTime", listdata.get(p).getPostingtime());
                    i.putExtra("aTime", listdata.get(p).getAddingtime());
                    i.putExtra("Answer", listdata.get(p).getAnswer());
                    i.putExtra("dTime", holder.dtime.getText().toString());
                    i.putExtra("SSS", listdata.get(p).getSss());
                    context.startActivity(i);
                }
            });
        }


    }

    String setttime(String time1, String time2) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date1 = simpleDateFormat.parse(time1);
        Date date2 = simpleDateFormat.parse(time2 + ":00");
        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());
        long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;


        return differenceInHours + ":" + differenceInMinutes;

    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llout;
        public TextView ptime, atime, dtime, type;

        public ViewHolder(View itemView) {
            super(itemView);
            this.llout =  itemView.findViewById(R.id.llout);
            this.type =  itemView.findViewById(R.id.type);
            this.ptime =  itemView.findViewById(R.id.ptime);
            this.atime =  itemView.findViewById(R.id.atime);
            this.dtime =  itemView.findViewById(R.id.dtime);
        }
    }

}