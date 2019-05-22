package com.example.camera_beauty.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.camera_beauty.R;
import com.example.camera_beauty.activtiy.MainActivity;

import org.opencv.android.CameraBridgeViewBase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

public class RecycleviewmainBottomadapter extends RecyclerView.Adapter<RecycleviewmainBottomadapter.viewholder> {
    private Bitmap bitmap = null;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private Context context1;
    private SeekBar seekBar;
    public void setBitmap(Bitmap bitmaptime){
        bitmap = bitmaptime;
    }
    public RecycleviewmainBottomadapter(Bitmap bitmap, CameraBridgeViewBase cameraBridgeViewBase, Context context, SeekBar seekBar){
        this.bitmap = bitmap;
        this.cameraBridgeViewBase = cameraBridgeViewBase;
        context1 = context;
        this.seekBar = seekBar;
    }
    @NonNull
    @Override
    public RecycleviewmainBottomadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_main_bottom,parent,false);
        RecycleviewmainBottomadapter.viewholder viewholder1 = new RecycleviewmainBottomadapter.viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleviewmainBottomadapter.viewholder holder, final int position) {
        bitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
        holder.imageView.setImageBitmap(bitmap);
        holder.imageView.setImageResource(R.drawable.picture1);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(position==3){
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(seekBar,"translationY",10);
                        objectAnimator.setDuration(200);
                        objectAnimator.start();
                    }else {
//                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(seekBar,"translationY",10);
//                        objectAnimator.setDuration(200);
//                        objectAnimator.start();
                    }
                    MainActivity.markfilter = position;
                    Log.d("xiaotaonihao", String.valueOf(position));
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_mian_bottom);
        }
    }
}
