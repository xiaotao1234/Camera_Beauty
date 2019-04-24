package com.example.camera_beauty.activtiy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.camera_beauty.R;
import com.example.camera_beauty.adapter.Recycleviewoneadapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ShowOneActivity extends AppCompatActivity {
    private ArrayList<String> filename;
    RecyclerView recyclerView;
    Recycleviewoneadapter recycleviewoneadapter;
    private int currentid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_show);
        recyclerView = findViewById(R.id.show_one);
        filename = getIntent().getStringArrayListExtra(Exhibition.keyofbitmap);
        currentid = getIntent().getIntExtra(Exhibition.currentid,0);
        recycleviewoneadapter = new Recycleviewoneadapter(filename);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recycleviewoneadapter);
        recyclerView.smoothScrollToPosition(currentid);
    }
}
