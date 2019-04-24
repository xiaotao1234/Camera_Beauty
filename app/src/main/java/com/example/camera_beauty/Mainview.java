package com.example.camera_beauty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class Mainview extends RelativeLayout {
    Context context;
    int downx,downy;
    private upListener upListener1;
    public void setLisener(upListener upListenerer){
        upListener1 = upListenerer;
    }
    public interface upListener{
        public void itemuplistener();
        public void itemdownlistener();
    }
    public Mainview(Context context) {
        super(context);
        this.context = context;
    }

    public Mainview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Mainview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downx = (int) event.getRawX();
                downy = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int y = (int) event.getRawY();
                if(downy-y>30){
                    upListener1.itemuplistener();
                }
                if(y-downy>30){
                    upListener1.itemdownlistener();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
