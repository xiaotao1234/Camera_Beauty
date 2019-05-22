package com.example.camera_beauty.activtiy;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.camera_beauty.Mainview;
import com.example.camera_beauty.R;
import com.example.camera_beauty.Take_a_photo_view;
import com.example.camera_beauty.adapter.RecyclemainbgAdapter;
import com.example.camera_beauty.adapter.RecycleviewmainBottomadapter;
import com.example.camera_beauty.adapter.earadapter;
import com.example.camera_beauty.adapter.emojionadapter;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList<String> filenameshow = new ArrayList<>();
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LinearLayout changeover;
    private TextView showtimeout;
    private int CameraId;
    private Context context;
    private ImageView filterbutton;
    private SeekBar seekBar;
    private boolean DelayedMark = false;
    private filteraddlistener filteraddlistener1;
    private TextView delay_3;
    private TextView delay_5;
    private TextView delay_10;
    private Handler handler;
    private int width;
    private int height;
    private Bitmap bitmap2;
    private Mat mat2;
    private Rect rect;
    private int newWidth;
    private int newHeight;
    private float scaleWidth;
    private float scaleHeight;
    private Bitmap newbm;
    private RecyclerView emojionrecycle;
    private int emojresuorce = R.drawable.emoji1;
    private int earresuorce = 0;
    private Mat mat3;
    private Bitmap bitmap3;
    private Bitmap secondbit;
    private int widths;
    private int heights;
    private float scalew;
    private float scaleh;
    private Rect rectear;
    private Mat matearbg;
    private Bitmap bitmapear;
    private Bitmap bitmapbg;
    private int widthear;
    private int heightear;
    private Bitmap bitmapearb;
    private Mat matear;
    private Bitmap bitmapearbgb;
    private int widthearbg;
    private int heightearbg;
    private float scaleearw;
    private float scaleearh;
    private float scaleearwbg;
    private float scaleearhbg;
    private Bitmap bitmapearbg;
    private int newWidthear;
    private int newHeightear;
    private RecyclerView earrecycle;
    private RecyclerView mainbgrecycle;
    private int[] earlist;
    private int[] earelist;
    private int[] earbglist;
    private int[] emojexhi;
    private int[] mainbglist;
    private Mat mat1;
    private Mat mat;
    private int bgsource;
    private Mat bgmat;
    private int widthbg;
    private int heightbg;

    public void setfilter(filteraddlistener filteraddlistener1){
        this.filteraddlistener1 = filteraddlistener1;
    }
    public interface filteraddlistener{
        Mat set(Mat mat);
    }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        showtimeout = findViewById(R.id.showtimeout);
        delay_3 = findViewById(R.id.delay_3);
        delay_5 = findViewById(R.id.delay_5);
        delay_10 = findViewById(R.id.delay_10);
        seekBar = findViewById(R.id.dimline);
        mainbgrecycle = findViewById(R.id.mainbgrecycle);
        filterbutton = findViewById(R.id.filter_button);
        context = getApplicationContext();
        changeover = findViewById(R.id.change_emoj);
        linearLayout = findViewById(R.id.delay_all);
        delayimage = findViewById(R.id.delay_button);
        recyclerView = findViewById(R.id.template_list);
        getphoto = findViewById(R.id.take_photo);
        show = findViewById(R.id.show);
        frameLayout = findViewById(R.id.Photo_List);
        emojionrecycle = findViewById(R.id.emojionchange);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showtimeout.setText(String.valueOf((Integer) msg.obj));
                if((Integer)msg.obj==0){
                    showtimeout.setVisibility(View.INVISIBLE);
                }
            }
        };
        delay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaytime = 3000;
            }
        });
        delay_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaytime = 5000;
            }
        });
        delay_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaytime = 10000;
            }
        });
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if(frameLayout.getHeight()!= 0){
                    frameLayout.setTranslationY(frameLayout.getHeight());
                    frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        seekBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                seekBar.setTranslationY(100);
                seekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            filenameshow.add(new String(data, 0, data.length - 1));
        }
        Glide.with(context).load(filenameshow.get(filenameshow.size()-1)).into(show);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initevents(){
        emojexhi = new int[]{
                R.drawable.emoji1,
                R.drawable.emoji2,
                R.drawable.emoji3,
                R.drawable.emoji4,
                R.drawable.emoji5,
                R.drawable.emoji6,
                R.drawable.emoji7,
                R.drawable.emoji8,
                R.drawable.emoji9,
                R.drawable.emoji10,
                R.drawable.emoji11,
                R.drawable.emoji12,
                R.drawable.emoji13
        };
        earlist = new int[]{
                R.drawable.ear1,
                R.drawable.ear2,
                R.drawable.ear3,
                R.drawable.ear4,
                R.drawable.ear6,
                R.drawable.ear7,
                R.drawable.ear9,
                R.drawable.ear10,
                R.drawable.ear11,
                R.drawable.ear12
        };
        earelist = new int[]{
                R.drawable.eare1,
                R.drawable.eare6
        };
        earbglist = new int[]{
                R.drawable.ear1bg,
                R.drawable.ear6bg
        };
        mainbglist = new int[]{
                R.drawable.mainbg1,
                R.drawable.mainbg2,
                R.drawable.mainbg3,
                R.drawable.mainbg4,
                R.drawable.mainbg5,
                R.drawable.mainbg6
        };
        emojionadapter emojionadapter = new emojionadapter(emojexhi);
        emojionrecycle = findViewById(R.id.emojionchange);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        emojionrecycle.setLayoutManager(linearLayoutManager);
        emojionrecycle.setAdapter(emojionadapter);
        emojionadapter.setlistneremo(new emojionadapter.emojback() {
            @Override
            public void emojListener(int a) {
                emojresuorce = a;
            }
        });
        earrecycle = findViewById(R.id.earchange);
        LinearLayoutManager linearLayoutManagerear = new LinearLayoutManager(this);
        linearLayoutManagerear.setOrientation(LinearLayoutManager.HORIZONTAL);
        earrecycle.setLayoutManager(linearLayoutManagerear);
        earadapter earadapters = new earadapter(earlist);
        earadapters.setSetearlistener(new earadapter.earlistener() {
            @Override
            public void earlistener(int position) {
                earresuorce = position;
            }
        });
        earrecycle.setAdapter(earadapters);
        RecyclemainbgAdapter recyclemainbgAdapter = new RecyclemainbgAdapter(mainbglist);
        recyclemainbgAdapter.setMainBgListener(new RecyclemainbgAdapter.mainBgListener() {
            @Override
            public void setbg(int set) {
                bgsource = set;
            }
        });
        LinearLayoutManager linearLayoutManagerbg = new LinearLayoutManager(this);
        linearLayoutManagerbg.setOrientation(LinearLayoutManager.HORIZONTAL);
        mainbgrecycle.setLayoutManager(linearLayoutManagerbg);
        mainbgrecycle.setAdapter(recyclemainbgAdapter);
        seekBar.setMax(100);
        seekBar.setMin(1);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarnum = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

//                LinearLayoutManager linearLayoutManager =
//                if(CameraId == 0){
//                    Log.d("xiaotaoshi","da");
//                    Intent intent = new Intent(context, MainActivity.class);
//                    intent.putExtra(TAG,1);
//                    startActivity(intent);
//                }else {
//                    Intent intent = new Intent(context, MainActivity.class);
//                    intent.putExtra(TAG,0);
//                    startActivity(intent);
//                }

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
                show.setVisibility(View.INVISIBLE);
                showtimeout.setVisibility(View.VISIBLE);
                if(delaytime != 0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int a = delaytime;
                            while(a>0){
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                a = a-1000;
                                Message message = Message.obtain();
                                message.obj = a/1000;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                }
                getphoto.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bitmap = openCvCameraView.getmCacheBitmap();
                        if(bitmap==null) {
                            Log.d("file_lll", "ddadad");
                        }
                        show.setVisibility(View.VISIBLE);
                        show.setImageBitmap(bitmap);
                        Log.d("xiaotaonihao","showchange");
                        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else {
                            saveintocamera(bitmap);
                        }
                    }
                },delaytime);
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
    private int delaytime = 0;
    public void saveintocamera(Bitmap bitmap){
        Log.d("xiaotao","in save");
        File file1 = getOutputMediaFile();
        Log.d("xiaotao",file1.getAbsolutePath().toString());
        if (file1.exists()){
            file1.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String saveAs = file1.getAbsolutePath();
        Uri contentUri = Uri.fromFile(new File(saveAs));
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
        sendBroadcast(mediaScanIntent);
    }
    public static File getOutputMediaFile() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File mediaStorageDir = new File(getCameraPath(file));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public File save(Bitmap bitmap){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File filestrong = new File(getCameraPath(file));
        if(!filestrong.exists()){
            if(!filestrong.mkdirs()){
                return null;
            }
        }
        String timestring = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.CHINESE).format(new Date());
        File mediafile = new File(filestrong.getPath()+File.separator+"IMG_"+timestring+".jpg");
        return mediafile;
    }

    private static String getCameraPath(File dcim) {//获得相册目录
        Log.d("xiaotao",dcim.getPath().toString());
        try {
            for (File file : dcim.listFiles()) {
                if ("camera".equalsIgnoreCase(file.getName())) {
                    return file.getAbsolutePath();
                }
            }
        } catch (Exception e) {
        }
        return dcim.getAbsolutePath();
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
        mat1 = new Mat();
        mat = new Mat();
        bgmat = new Mat();
        bitmapbg = BitmapFactory.decodeResource(getResources(),mainbglist[bgsource]);
        widthbg = bitmapbg.getWidth();
        heightbg = bitmapbg.getHeight();
        Log.d("xiaotaoxiao",widthbg+":"+heightbg);
        float scalewbg = (float) 1072/widthbg;
        float scalehbg = (float) 1072/heightbg;
        Matrix matrixbg = new Matrix();
        matrixbg.postScale(scalewbg,scalehbg);
        bitmapbg = Bitmap.createBitmap(bitmapbg, 0, 0, widthbg, heightbg, matrixbg,true);
        Utils.bitmapToMat(bitmapbg,bgmat);
        mRgba = inputFrame.rgba(); //RGBA
        mGray = inputFrame.gray(); //单通道灰度图
        mat1 = mGray;
        mat = mRgba;
        Configuration configuration = this.getResources().getConfiguration();
        if(configuration.orientation==Configuration.ORIENTATION_PORTRAIT){
            Core.rotate(mRgba, mat,Core.ROTATE_90_COUNTERCLOCKWISE );
            Core.rotate(mGray, mat1,Core.ROTATE_90_COUNTERCLOCKWISE );
        }
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
            for (int i = 0; i < (facesArray.length>1?1:facesArray.length); i++) {    //用框标记
                Log.d("xiaotaonihaohaot1", String.valueOf(facesArray[i].tl().x));
                Log.d("xiaotaonihaohaobr", String.valueOf(facesArray[i].br().x));
                rect = new Rect();
                rect.x = (int) facesArray[i].tl().x;
                rect.y = (int) facesArray[i].tl().y;
                rect.width = (int) (facesArray[i].br().x-facesArray[i].tl().x);
                rect.height = (int) (facesArray[i].br().y-facesArray[i].tl().y);
                rectear = new Rect();
                rectear.x = (int) facesArray[i].tl().x;
                rectear.y = (int) facesArray[i].tl().y;
                rectear.width = (int) (facesArray[i].br().x-facesArray[i].tl().x);
                rectear.height = (int) (facesArray[i].br().y-facesArray[i].tl().y)/3;
                matearbg = new Mat(rectear.width,rectear.height,CvType.CV_8UC4);
                matear = new Mat(rectear.width, rectear.height, CvType.CV_8UC4);
                bitmapearbg = BitmapFactory.decodeResource(getResources(), earbglist[earresuorce]);
                bitmapear = BitmapFactory.decodeResource(getResources(),earelist[earresuorce]);
                widthearbg = bitmapearbg.getWidth();
                widthear = bitmapear.getWidth();
                heightearbg = bitmapearbg.getHeight();
                heightear = bitmapear.getHeight();
                mat2 = new Mat(new Size((facesArray[i].br().x-facesArray[i].tl().x),(facesArray[i].br().y-facesArray[i].tl().y)),CvType.CV_8UC4);
                bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.emoji_bg);
                mat3 = new Mat(new Size((facesArray[i].br().x-facesArray[i].tl().x),(facesArray[i].br().y-facesArray[i].tl().y)),CvType.CV_8UC4);
                bitmap3 = BitmapFactory.decodeResource(getResources(),emojresuorce);
                width = bitmap2.getWidth();
                height = bitmap2.getHeight();
                widths = bitmap3.getWidth();
                heights = bitmap3.getHeight();
                // 设置想要的大小
                newWidth = rect.width;
                newHeight = rect.height;
                newWidthear = rectear.width;
                newHeightear = rectear.height;
                // 计算缩放比例
                scalew = (float)newWidth/widths;
                scaleh = (float) newHeight/heights;
                scaleWidth = ((float) newWidth) / width;
                scaleHeight = ((float) newHeight) / height;
                scaleearw = (float)newWidthear/widthear;
                scaleearh = (float)newHeightear/heightear;
                scaleearwbg = (float) newWidthear / widthearbg;
                scaleearhbg = (float) newHeightear / heightearbg;
                // 取得想要缩放的matrix参数
                Matrix matrix1 = new Matrix();
                Matrix matrix = new Matrix();
                Matrix matrixear = new Matrix();
                Matrix matrixearbg = new Matrix();
                matrix1.postScale(scalew,scaleh);
                matrix.postScale(scaleWidth, scaleHeight);
                matrixear.postScale(scaleearw, scaleearh);
                matrixearbg.postScale(scaleearwbg,scaleearhbg);
                // 得到新的图片
                newbm = Bitmap.createBitmap(bitmap2, 0, 0, width, height, matrix,true);
                Utils.bitmapToMat(newbm, mat2);
                secondbit = Bitmap.createBitmap(bitmap3,0,0,widths,heights,matrix1,true);
                Utils.bitmapToMat(secondbit,mat3);
                bitmapearb = Bitmap.createBitmap(bitmapear,0,0,widthear,heightear,matrixear,true);
                Utils.bitmapToMat(bitmapearb,matear);
                bitmapearbgb = Bitmap.createBitmap(bitmapearbg,0,0,widthearbg,heightearbg,matrixearbg,true);
                Utils.bitmapToMat(bitmapearbgb,matearbg);
                Log.d("xiaotaonihaohao", String.valueOf(rect.width== mat2.width()?1:2));
                Log.d("xiaotaonihaohao", String.valueOf(rect.height== mat2.height()?1:2));
                Core.subtract(mat.submat(rectear),matearbg, mat.submat(rectear));
                Core.add(mat.submat(rectear),matear, mat.submat(rectear));
                Core.subtract(mat.submat(rect),mat2, mat.submat(rect));
                Core.add(mat.submat(rect),mat3, mat.submat(rect));
            }
        }
        mat = filterset(mat);
        Core.flip(mat, mat,1);
        Core.addWeighted(mat,0.6,bgmat,0.4,0,mat);
        return mat;
    }
    public static int markfilter = 0;
    public static int seekbarnum = 1;
    public Mat filterset(Mat mat){
        switch (markfilter){
            case 0:
            break;
            case 1:
                Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
                Core.bitwise_not(mat,mat);
                Imgproc.threshold(mat,mat,100,255,Imgproc.THRESH_BINARY_INV);
                break;
            case 2:
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
                break;
            case 3:
                Imgproc.blur(mat, mat, new Size(seekbarnum, seekbarnum));
                break;
            case 4:

                default:
                    break;
        }
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
        f = new RecycleviewmainBottomadapter(bitmap,openCvCameraView,getApplicationContext(),seekBar);
        recyclerView.setAdapter(f);
        f.notifyDataSetChanged();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(frameLayout,"translationY",-100);
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

