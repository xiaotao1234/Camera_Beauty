package com.example.camera_beauty.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.camera_beauty.R;
import com.example.camera_beauty.activtiy.Exhibition;
import com.example.camera_beauty.activtiy.ShowOneActivity;
import com.example.camera_beauty.utils.Album;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleItemAdapter extends RecyclerView.Adapter<RecycleItemAdapter.viewholder> {
    ArrayList<String> filepath;
    boolean judge = false;
    int[] check;
    Context context;
    Bitmap bitmap = null;
    FileInputStream fileInputStream;
    BitmapFactory.Options options = new BitmapFactory.Options();
    private clicklistener clicklistener2;
    private ImageView imageView;
    public void setclickliener(clicklistener clicklistener1){
        clicklistener2 = clicklistener1;
    }
    public interface clicklistener{
        public void clicklistener();
    }
    public RecycleItemAdapter(ArrayList<String> filepath,ImageView imageView){
        this.filepath = filepath;
        check = new int[filepath.size()];
        Arrays.fill(check,0);
        this.imageView = imageView;
    }
    private List<Album> mAlbumList = new ArrayList<>();
    public void refreshData(List<Album> data) {
        if (data != null) {
            mAlbumList.clear();
            mAlbumList.addAll(data);
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem,parent,false);
        view.getLayoutParams().width = parent.getContext().getResources().getDisplayMetrics().widthPixels/3;
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        if(judge == true){
            holder.checkBox.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }else {
            holder.checkBox.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
        Glide.with(context).load(filepath.get(filepath.size()-position-1)).override(350,350).into(holder.imageView);
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
                if(clicklistener2!= null){
                    clicklistener2.clicklistener();
                }
                notifyDataSetChanged();
                notifyItemRangeChanged((position-15)>0?(position-15):0,(position+15)<filepath.size()?(position+15):(filepath.size()-1));
                Log.d("xiaotaonihao","long");
                Exhibition.checkid = true;
                return true;
            }
        });
    }
    public void setcheckboxtrue(){
        judge = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filepath.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView checkBox;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.image_check);
            imageView = itemView.findViewById(R.id.listview);

        }
    }
}
