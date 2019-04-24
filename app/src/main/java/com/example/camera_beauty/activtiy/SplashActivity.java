package com.example.camera_beauty.activtiy;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.camera_beauty.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private View mLlContent;
    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLlContent = findViewById(R.id.ll_content);
        playLogoAnim();
    }

    private void playLogoAnim() {
        mAnimator = ObjectAnimator.ofFloat(mLlContent, "alpha", 0f, 1f);
        mAnimator.setDuration(100);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mLlContent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goActivity(MainActivity.class);
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
