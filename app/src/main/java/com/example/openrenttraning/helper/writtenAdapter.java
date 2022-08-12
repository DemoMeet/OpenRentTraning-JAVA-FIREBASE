package com.example.openrenttraning.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.openrenttraning.R;

import java.util.List;

public class writtenAdapter extends RecyclerView.Adapter<writtenAdapter.ViewHolder>{

    List<String> listdata;

    public writtenAdapter(List<String> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_written_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int p) {
        int position = p;
        holder.textView.setText(listdata.get(position));
        holder.serial.setText(String.valueOf(position+1)+ ". ");
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listdata.remove(position);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton imageView;
        public TextView textView, serial;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView =  itemView.findViewById(R.id.delete);
            this.textView = itemView.findViewById(R.id.textview);
            this.serial = itemView.findViewById(R.id.serial);
        }
    }
}