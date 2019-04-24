package com.example.camera_beauty.activtiy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.camera_beauty.R;
import com.example.camera_beauty.activtiy.MainActivity;
import com.example.camera_beauty.adapter.RecycleItemAdapter;

import java.util.ArrayList;

public class Exhibition extends AppCompatActivity {
    public static String keyofbitmap = "showphoto";
    public static String currentid = "CURRENT_ID";
    private ArrayList<String> filename;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);
        filename = getIntent().getStringArrayListExtra(keyofbitmap);
        Log.d("xiaotaonihao",filename.toString());
        recyclerView = findViewById(R.id.exhibition_list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        RecycleItemAdapter recycleItemAdapter = new RecycleItemAdapter(filename);
        recyclerView.setAdapter(recycleItemAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
