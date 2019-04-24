package com.example.camera_beauty;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Take_a_photo_view extends View {
    private int radiusx,radiusy,radius;
    private Paint paint;
    private Paint linepaint;
    private OnItemListener listener;
    public void setItemListener(OnItemListener listener){
        this.listener = listener;
    }
    public interface OnItemListener{
        public void OnItemSelectListener();
    }
    public Take_a_photo_view(Context context) {
        super(context);
        init();
    }

    public Take_a_photo_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Take_a_photo_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        paint = new Paint();
        linepaint = new Paint();
        paint.setAntiAlias(true);
        linepaint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#BB000000"));
        linepaint.setColor(Color.parseColor("#77000000"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(Color.parseColor("#77000000"));
                linepaint.setColor(Color.parseColor("#BB000000"));
                Log.d("xiaotao","down");
                break;
            case MotionEvent.ACTION_UP:
                listener.OnItemSelectListener();
                paint.setColor(Color.parseColor("#BB000000"));
                linepaint.setColor(Color.parseColor("#77000000"));
                Log.d("xiaotao","up");
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


//+

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radiusx = (right+left)/2;
        radiusy = (top+bottom)/2;
        radius = (left-right)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(85,85,85,linepaint);
        canvas.drawCircle(85,85,70,paint);
    }
}
