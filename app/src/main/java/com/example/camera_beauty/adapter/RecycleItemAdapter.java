package com.example.camera_beauty.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.camera_beauty.R;
import com.example.camera_beauty.activtiy.Exhibition;
import com.example.camera_beauty.activtiy.ShowOneActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleItemAdapter extends RecyclerView.Adapter<RecycleItemAdapter.viewholder> {
    ArrayList<String> filepath;
    boolean judge = false;
    int[] check;
    Context context;
    Bitmap bitmap = null;
    private clicklistener clicklistener2;
    public void setclickliener(clicklistener clicklistener1){
        clicklistener2 = clicklistener1;
    }
    public interface clicklistener{
        public void clicklistener();
    }
    public RecycleItemAdapter(ArrayList<String> filepath){
        this.filepath = filepath;
        check = new int[filepath.size()];
        Arrays.fill(check,0);
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem,parent,false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        if(judge == true){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;
            FileInputStream fileInputStream = context.openFileInput(filepath.get(position));
            bitmap = BitmapFactory.decodeStream(fileInputStream,null,options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(bitmap==null){
            Log.d("xiaotaonihao","bitmap is null");
        }
//        if(position%2==0){
//            Mat mat2 = new Mat();
//            Utils.bitmapToMat(bitmap,mat2);
//            mat2.setTo(new Scalar(100,0,0));
//            Utils.matToBitmap(mat2,bitmap);
//        }
        Glide.with(context).load(bitmap).into(holder.imageView);
        holder.checkBox.setChecked(check[position]==1);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xiaotaonihao","xa");
                Intent intent = new Intent(context,ShowOneActivity.class);
                intent.putExtra(Exhibition.keyofbitmap,filepath);
                intent.putExtra(Exhibition.currentid,position);
                context.startActivity(intent);
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                judge = true;
                notifyDataSetChanged();
                Log.d("xiaotaonihao","long");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filepath.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.image_check);
            imageView = itemView.findViewById(R.id.listview);
        }
    }
}
