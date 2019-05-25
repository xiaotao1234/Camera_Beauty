package com.example.camera_beauty.activtiy;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.camera_beauty.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayoutcamera;
    LinearLayout linearLayoutgallery;
    ImageView set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        set = findViewById(R.id.set);
        linearLayoutcamera = findViewById(R.id.camera);
        linearLayoutgallery = findViewById(R.id.gallery);
        linearLayoutcamera.setOnClickListener(this);
        linearLayoutgallery.setOnClickListener(this);
        linearLayoutgallery.setSystemUiVisibility(View.INVISIBLE);
        set.setOnClickListener(this);
        isExistMainActivity(MainActivity.class);
    }
    private void goActivity(final Class<?> cls) {
        startActivity(new Intent(StartActivity.this, cls));
    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayoutgallery.setSystemUiVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera:
                goActivity(MainActivity.class);
                break;
            case R.id.gallery:
                goActivity(Exhibition.class);
                break;
            case R.id.set:
                goActivity(SettingActivity.class);
        }
    }
    private boolean isExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) {// 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);//获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.topActivity.equals(cmpName)) {// 说明它已经启动了
                    flag = true;
                    break;//跳出循环，优化效率
                }
            }
        }
        return flag;//true 存在 falese 不存在
    }
}
