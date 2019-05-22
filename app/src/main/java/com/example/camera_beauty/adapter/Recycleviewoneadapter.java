package com.example.camera_beauty.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.camera_beauty.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Recycleviewoneadapter extends RecyclerView.Adapter<Recycleviewoneadapter.viewholder> {
    Context context;
    ArrayList<String> filepath;
    Bitmap bitmap = null;
    public Recycleviewoneadapter(ArrayList<String> filepath) {
        this.filepath = filepath;
    }

    @NonNull
    @Override
    public Recycleviewoneadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_one_item,parent,false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycleviewoneadapter.viewholder holder, int position) {
        Glide.with(context).load(filepath.get(filepath.size()-position-1)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return filepath.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_one_item);
        }
    }
}
