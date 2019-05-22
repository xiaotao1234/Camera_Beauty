package com.example.camera_beauty.activtiy;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.camera_beauty.R;
import com.example.camera_beauty.adapter.Recycleviewoneadapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class ShowOneActivity extends AppCompatActivity {
    private ArrayList<String> filename;
    RecyclerView recyclerView;
    private static final float MILLISECONDS_PER_INCH = 40f;
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
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(currentid);
        recyclerView.setAdapter(recycleviewoneadapter);
    }
}
