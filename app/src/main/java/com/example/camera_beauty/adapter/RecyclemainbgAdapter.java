package com.example.camera_beauty.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.camera_beauty.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclemainbgAdapter extends RecyclerView.Adapter<RecyclemainbgAdapter.viewholder> {
    private mainBgListener mainBgListener1;
    public void setMainBgListener(mainBgListener mainBgListener1){
        this.mainBgListener1 = mainBgListener1;
    }
    public interface mainBgListener{
        public void setbg(int set);
    }
    int[] bglist;
    public RecyclemainbgAdapter(int[] bglist) {
        this.bglist = bglist;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earitem,parent,false);
        viewholder viewholder = new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        holder.imageView.setImageResource(bglist[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBgListener1.setbg(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bglist.length;
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.earimage);
        }
    }
}
