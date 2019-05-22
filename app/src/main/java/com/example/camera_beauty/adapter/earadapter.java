package com.example.camera_beauty.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.camera_beauty.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class earadapter extends RecyclerView.Adapter<earadapter.viewholder> {
    private earlistener earlistener;
    public void setSetearlistener(earlistener earlistener){
        this.earlistener = earlistener;
    }
    public interface earlistener{
        void earlistener(int position);
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earitem,parent,false);
        viewholder viewholder = new viewholder(view);
        return viewholder;
    }
    private int[] ears;
    public earadapter(int[] ears) {
        this.ears = ears;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        holder.imageView.setImageResource(ears[position]);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earlistener.earlistener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ears.length;
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        RelativeLayout relativeLayout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.earimage);
            relativeLayout = itemView.findViewById(R.id.cotains_ear);
        }
    }
}
