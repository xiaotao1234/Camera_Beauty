package com.example.camera_beauty.activtiy;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.camera_beauty.Mainview;
import com.example.camera_beauty.R;
import com.example.camera_beauty.Take_a_photo_view;
import com.example.camera_beauty.adapter.RecycleviewmainBottomadapter;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2,CameraBridgeViewBase.CvCameraViewListener {

    private CameraBridgeViewBase openCvCameraView;
    private static final String TAG = "OpencvActivity";
    private CascadeClassifier cascadeClassifier = null; //级联分类器
    private Mat mRgba; //图像容器
    private Mat mGray;
    Bitmap bitmap1 = null;
    Bitmap bitmap;
    private int absoluteFaceSize = 0;
    private Take_a_photo_view getphoto;
    RecycleviewmainBottomadapter f;
    private ImageView show;
    private LinearLayout delayimage;
    private ArrayList<String> filename = new ArrayList<>();
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LinearLayout changeover;
    private int CameraId;
    private Context context;
    private ImageView filterbutton;
    private int delaysign = 0;
    private boolean DelayedMark = false;
    private void initializeOpenCVDependencies() {
        try {
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface); //OpenCV的人脸模型文件： lbpcascade_frontalface_improved
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface_improved.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            // 加载cascadeClassifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Error loading cascade", e);
        }
        // 显示
        openCvCameraView.enableView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        Log.d(TAG,"initview invoke");
        initevents();
        Log.d(TAG,"initview invoke");

    }

    public void initview(){
        filterbutton = findViewById(R.id.filter_button);
        context = getApplicationContext();
        changeover = findViewById(R.id.change_over);
        linearLayout = findViewById(R.id.delay_all);
        delayimage = findViewById(R.id.delay_button);
        recyclerView = findViewById(R.id.template_list);
        getphoto = findViewById(R.id.take_photo);
        show = findViewById(R.id.show);
        frameLayout = findViewById(R.id.Photo_List);
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if(frameLayout.getHeight()!= 0){
                    frameLayout.setTranslationY(frameLayout.getHeight()+100);
                    frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if(linearLayout.getHeight()!=0){
                    linearLayout.setTranslationX(linearLayout.getWidth());
                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void initevents(){
        bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.more);
        CameraId = getIntent().getIntExtra(TAG,1);
        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimtor();
                getphoto.setVisibility(View.INVISIBLE);
            }
        });
        changeover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CameraId == 0){
                    Log.d("xiaotaoshi","da");
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra(TAG,1);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra(TAG,0);
                    startActivity(intent);
                }
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Exhibition.class);
                intent.putExtra(Exhibition.keyofbitmap,filename);
                startActivity(intent);
                finish();
            }
        });
        delayimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DelayedMark==false){
                    delaysign = 2;
                    startdelayAnimtorappear();
                    DelayedMark = true;
                }else {
                    startdelayAnimtorhidden();
                    DelayedMark = false;
                }
                Log.d("xttest","delayimage click event invoked");
            }
        });
        Mainview mainview = findViewById(R.id.main_mm);
        mainview.setLisener(new Mainview.upListener() {
            @Override
            public void itemuplistener() {
                Log.d("xiaotaonihao","get");
                startAnimtor();
                getphoto.setVisibility(View.INVISIBLE);
            }

            @Override
            public void itemdownlistener() {
                hide();
            }
        });
//        linearLayout.setSystemUiVisibility(View.INVISIBLE);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }else {
            takephoto(CameraId);
        }
//        getphoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bitmap = openCvCameraView.getmCacheBitmap();
//                if(bitmap==null) {
//                    Log.d("file_lll", "ddadad");
//                }
//                show.setImageBitmap(bitmap);
//                filename.add(save(bitmap));
//            }
//        });
        getphoto.setItemListener(new Take_a_photo_view.OnItemListener() {
            @Override
            public void OnItemSelectListener() {
                getphoto.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bitmap = openCvCameraView.getmCacheBitmap();
                        if(bitmap==null) {
                            Log.d("file_lll", "ddadad");
                        }
                        show.setImageBitmap(bitmap);
                        Log.d("xiaotaonihao","showchange");
                        filename.add(save(bitmap));
                    }
                },0);
            }
        });
//        getphoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bitmap = openCvCameraView.getmCacheBitmap();
//                if(bitmap==null) {
//                    Log.d("file_lll", "ddadad");
//                }
//                show.setImageBitmap(bitmap);
//                filename = save(bitmap);
//            }
//        });
    }

    public String save(Bitmap bitmap){
        String name = "photo"+System.currentTimeMillis();
        try {
            FileOutputStream fileOutputStream = openFileOutput(name.toString(),Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Log.d("file_name",fileOutputStream.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void takephoto(int cameraid){
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.javaCameraView);
        openCvCameraView.setCameraIndex(1); //摄像头索引        -1/0：后置双摄     1：前置
        openCvCameraView.enableFpsMeter(); //显示FPS
        openCvCameraView.setCvCameraViewListener((CameraBridgeViewBase.CvCameraViewListener2) this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    takephoto(CameraId);
                }else {
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume invoked" );
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV init error");
        }
        initializeOpenCVDependencies();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        Mat mat = new Mat();
        Core.rotate(inputFrame,mat,Core.ROTATE_180 );
        Log.d("xiaotaoxxx","come");
        return mat;
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mat1 = new Mat();
        Mat mat = new Mat();
        mRgba = inputFrame.rgba(); //RGBA
        mGray = inputFrame.gray(); //单通道灰度图
        Mat mat22 = new Mat(new Size(1072,1072),Imgproc.COLOR_RGB2BGRA);
        Mat mat23 = new Mat();
        Bitmap bitmapmarker = BitmapFactory.decodeResource(getResources(),R.drawable.more);
        Utils.bitmapToMat(bitmapmarker,mat22);
        Imgproc.cvtColor(mat22, mat23, Imgproc.COLOR_RGB2BGRA, 4);
        mat22.setTo(new Scalar(100,0,0));
        mat1 = mGray;
        mat = mRgba;
        Configuration configuration = this.getResources().getConfiguration();
        if(configuration.orientation==Configuration.ORIENTATION_PORTRAIT){
            Mat mat2 = new Mat();
            mRgba.copyTo(mat2);
            Core.rotate(mRgba,mat,Core.ROTATE_90_COUNTERCLOCKWISE );
            Core.rotate(mGray,mat1,Core.ROTATE_90_COUNTERCLOCKWISE );
            Imgproc.cvtColor(mat, mat2, Imgproc.COLOR_RGB2BGRA, 4);
            Core.add(mRgba,mat2,mRgba);
         
        }
        Log.d("xiaotaoxxx","come");
        if (absoluteFaceSize == 0) {
            int height = mat1.rows();
            if (Math.round(height * 0.2f) > 0) {
                absoluteFaceSize = Math.round(height * 0.1f);
            }
        } 
        //检测并显示vfr
        MatOfRect faces = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(mat1, faces, 1.1, 2, 2, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0){
            for (int i = 0; i < facesArray.length; i++) {    //用框标记

                Imgproc.rectangle(mat, facesArray[i].tl(), facesArray[i].br(), new Scalar(130, 132, 128, 255), 3);
//                Imgproc.drawMarker(mat,facesArray[1].tl(),new Scalar(100,0,0));
            }
        }
//        Range r = new Range(mat.width(), mat.height());
//        Log.d("xiaotaonihao", String.valueOf(mat.width()));
//        Mat ROI1 = new Mat(mat, r);
//        Range r1 = new Range(mRgba.width(), mRgba.height());
//        Mat ROI3 = new Mat(mRgba, r1);
//        double alpha = 0.75;
//        double beta;
//        beta = 1.0-alpha;
//        Core.addWeighted(ROI1, alpha, ROI3, beta, 0, mat1, -1);
////        Range range = new Range(1072,1072);
////        Mat mat2 = new Mat(mix,range);
        return mat;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(bitmap!=null){
            Log.e(TAG, "onpause is null");
        }
        Log.e(TAG, "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bitmap!=null){
            Log.e(TAG, "onstop is null");
        }
        Log.e(TAG, "onStop invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap==null){
            Log.e("xiaotaonihao", "ondestory is null");
        }
        Log.e(TAG, "onDestroy invoked");
    }
    public void startAnimtor(){
        filterbutton.setVisibility(View.INVISIBLE);
        show.setVisibility(View.INVISIBLE);
        bitmap = openCvCameraView.getmCacheBitmap();
        f = new RecycleviewmainBottomadapter(bitmap,openCvCameraView);
        recyclerView.setAdapter(f);
        f.notifyDataSetChanged();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(frameLayout,"translationY",-90);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        openCvCameraView.setlistener(new CameraBridgeViewBase.setbitmapchangelistener() {
            @Override
            public void changeListener() {
                bitmap = openCvCameraView.getmCacheBitmap();
                f.setBitmap(bitmap);
                getphoto.post(new Runnable() {
                    @Override
                    public void run() {
                        f.notifyDataSetChanged();
                    }
                });
            }
        });
        Log.d("xiaotaonihao","animtor");
    }
    public void startdelayAnimtorappear(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayout,"translationX",0);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
    public void startdelayAnimtorhidden(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayout,"translationX",linearLayout.getWidth());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
    public void hide(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(frameLayout,"translationY",+300);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getphoto.setVisibility(View.VISIBLE);
                show.setVisibility(View.VISIBLE);
                filterbutton.setVisibility(View.VISIBLE);
            }
        });
        Log.d("xiaotaonihao","hide");
        openCvCameraView.setlistener(null);
    }
}

