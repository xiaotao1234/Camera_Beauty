package com.example.camera_beauty.activtiy;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.camera_beauty.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {

    private View mLlContent;
    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLlContent = findViewById(R.id.ll_content);
        mLlContent.setSystemUiVisibility(View.INVISIBLE);
        playLogoAnim();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                    }else {
                        goActivity(StartActivity.class);
                    }
                }else {
                    finish();
                }
                break;
            case 2:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    goActivity(StartActivity.class);
                }else {
                    finish();
                }
        }
    }

    private void playLogoAnim() {
        mAnimator = ObjectAnimator.ofFloat(mLlContent, "alpha", 0f, 1f);
        mAnimator.setDuration(2500);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mLlContent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.CAMERA},1);
                }else {
                    if(ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                    }else {
                        goActivity(StartActivity.class);
                    }
                }
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }


    private void goActivity(final Class<?> cls) {
        startActivity(new Intent(SplashActivity.this, cls));
        finish();
    }
}
