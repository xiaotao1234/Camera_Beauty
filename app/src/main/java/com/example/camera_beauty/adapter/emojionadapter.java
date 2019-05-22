package com.example.camera_beauty.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.camera_beauty.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class emojionadapter extends RecyclerView.Adapter<emojionadapter.viewholder> {
    int[] imagesource;
    public emojback emojbackg;
    public void setlistneremo(emojback emojback){
        emojbackg = emojback;
    }
    public interface emojback{
        void emojListener(int a);
    }
    public emojionadapter(int[] imagesource) {
        this.imagesource = imagesource;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emojionitem,parent,false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        holder.imageView.setImageResource(imagesource[position]);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojbackg.emojListener(imagesource[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesource.length;
    }

    class viewholder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.cotains_emoj);
            imageView = itemView.findViewById(R.id.emojimage);
        }
    }
}
