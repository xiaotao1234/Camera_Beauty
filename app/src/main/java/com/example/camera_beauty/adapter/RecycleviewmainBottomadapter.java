package com.example.camera_beauty.adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.camera_beauty.R;

import org.opencv.android.CameraBridgeViewBase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

public class RecycleviewmainBottomadapter extends RecyclerView.Adapter<RecycleviewmainBottomadapter.viewholder> {
    private Bitmap bitmap = null;
    private GPUImage gpuImage;
    private CameraBridgeViewBase cameraBridgeViewBase;
    public void setBitmap(Bitmap bitmaptime){
        bitmap = bitmaptime;
    }
    public RecycleviewmainBottomadapter(Bitmap bitmap, CameraBridgeViewBase cameraBridgeViewBase){
        this.bitmap = bitmap;
        this.cameraBridgeViewBase = cameraBridgeViewBase;
    }
    @NonNull
    @Override
    public RecycleviewmainBottomadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_main_bottom,parent,false);
        RecycleviewmainBottomadapter.viewholder viewholder1 = new RecycleviewmainBottomadapter.viewholder(view);
        gpuImage = new GPUImage(parent.getContext());
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleviewmainBottomadapter.viewholder holder, int position) {
                bitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(new GPUImageSaturationFilter(100));
        bitmap = gpuImage.getBitmapWithFilterApplied();
        holder.imageView.setImageBitmap(bitmap);
        holder.imageView.setImageResource(R.drawable.picture1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xiaotaonihao","click in");
                cameraBridgeViewBase.setgpuimagelistener(new CameraBridgeViewBase.gpuimage() {
                    @Override
                    public Bitmap setgpuimagelistener(Bitmap bitmap) {
                        gpuImage.setImage(bitmap);
                        gpuImage.setFilter(new GPUImageGrayscaleFilter());
                        bitmap = gpuImage.getBitmapWithFilterApplied();
                        Log.d("xiaotaonihao","click invoke");
                        return bitmap;
                    }
                });
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
