package com.example.camera_beauty.activtiy;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.camera_beauty.Mainview;
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
    Mainview linearLayout;
    LinearLayout linearLayoutbo;
    boolean linbg = false;
    private int currentid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_show);
        recyclerView = findViewById(R.id.show_one);
        linearLayout = findViewById(R.id.oneall);
        linearLayoutbo = findViewById(R.id.onebottom);
        filename = getIntent().getStringArrayListExtra(Exhibition.keyofbitmap);
        currentid = getIntent().getIntExtra(Exhibition.currentid,0);
        recycleviewoneadapter = new Recycleviewoneadapter(filename);
        recycleviewoneadapter.setSetbger(new Recycleviewoneadapter.setbg() {
            @Override
            public void setbglistener() {
                Log.d("xiaobgxiao",linearLayout.getBackground().toString());
                if(linbg==false){
                    Log.d("xiaobgxiao",linearLayout.getBackground().toString());
                    linearLayout.setBackgroundColor(Color.parseColor("#000000"));
                    linearLayoutbo.setVisibility(View.INVISIBLE);
                    linbg = true;
                }else {
                    linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    linearLayoutbo.setVisibility(View.VISIBLE);
                    linbg = false;
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(currentid);
        recyclerView.setAdapter(recycleviewoneadapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.INVISIBLE);
    }
}
