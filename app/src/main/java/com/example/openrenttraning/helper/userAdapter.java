package com.example.openrenttraning.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.openrenttraning.R;
import com.example.openrenttraning.admin.AnswerViewActivity;
import com.example.openrenttraning.admin.SubmissionActivity;

import java.util.ArrayList;
import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder>  implements Filterable {

    List<String> listdata;
    List<String> listdata2;
    Context context;
    private List<String> exampleListFull;


    public userAdapter(List<String> listdata,List<String> listdata2, Context context) {
        this.listdata = listdata;this.context=context;this.listdata2 = listdata2;
        exampleListFull = new ArrayList<>(listdata2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_textview, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(listdata.get(position));;
        holder.textView2.setText(listdata2.get(position));
        holder.llout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AnswerViewActivity.class);
                i.putExtra("DATE", SubmissionActivity.date);
                i.putExtra("Email", listdata.get(position));
                i.putExtra("Name", listdata.get(position));
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata2.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;
        public LinearLayout llout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textview);
            this.textView2 = itemView.findViewById(R.id.textview2);
            this.llout = itemView.findViewById(R.id.llout);
        }
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : exampleListFull) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listdata2.clear();
            listdata2.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}