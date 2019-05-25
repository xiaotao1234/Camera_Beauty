package com.example.camera_beauty.activtiy;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.camera_beauty.R;
import com.example.camera_beauty.adapter.RecycleItemAdapter;
import com.example.camera_beauty.utils.Album;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class Exhibition extends AppCompatActivity {
    public static String keyofbitmap = "showphoto";
    public static String currentid = "CURRENT_ID";
    public static Boolean checkid = false;
    private ArrayList<String> filename = new ArrayList<>();
    RecyclerView recyclerView;
    RecycleItemAdapter recycleItemAdapter;
    ImageView moreimageview;
    ImageView deleteimageview;
    ImageView back;
    private deleteappear deleteappear;

    interface deleteappear {
        void clicklisenter();
    }

    public void setdelete(deleteappear deleteappear) {
        this.deleteappear = deleteappear;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);
        if(ContextCompat.checkSelfPermission(Exhibition.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"未取得授权",Toast.LENGTH_SHORT).show();
        }else {
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                filename.add(new String(data, 0, data.length - 1));
            }
            recyclerView = findViewById(R.id.exhibition_list);
            moreimageview = findViewById(R.id.more);
            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            moreimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    Uri shareUri = FileProvider.getUriForFile(
                            getApplicationContext(),
                            getApplicationContext().getPackageName() + ".fileprovider",
                            new File(filename.get(0)));
                    intent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    getApplication().startActivity(intent);
                }
            });
            deleteimageview = findViewById(R.id.delete);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recycleItemAdapter = new RecycleItemAdapter(filename, deleteimageview);
            recyclerView.setAnimation(null);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.setAdapter(recycleItemAdapter);
            recycleItemAdapter.setclickliener(new RecycleItemAdapter.clicklistener() {
                @Override
                public void clicklistener() {
                    deleteimageview.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setSystemUiVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GetAlbumTask(this).execute();
                } else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        if (checkid == true) {
            recycleItemAdapter.setcheckboxtrue();
            checkid = false;}
            super.onBackPressed();
//        } else {
//            super.onBackPressed();
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
    }

    class GetAlbumTask extends AsyncTask<Void, Integer, List<Album>> {

        WeakReference<Exhibition> mWeakReference;

        public GetAlbumTask(Exhibition context) {
            mWeakReference = new WeakReference<>(context);
            Log.d("xiaotao", "async in");
        }


        @Override
        protected List<Album> doInBackground(Void... voids) {
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String filePath = new String(data, 0, data.length - 1);
                Log.d("xiaoxiao", filePath);
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            Exhibition photoPickerActivity = mWeakReference.get();
            if (photoPickerActivity != null) {
                Album allAlbum = new Album();
                allAlbum.setName("ALL");
                if (!albums.isEmpty()) {
                    allAlbum.setCoverPath(albums.get(0).getCoverPath());
                }
                albums.add(0, allAlbum);
                photoPickerActivity.recycleItemAdapter.refreshData(albums);
            }
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
                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
                    Log.d("xiaoxiaoxiaoni",taskInfo.baseActivity.toString());
                    flag = true;
                }
            }
        }
        return flag;//true 存在 falese 不存在
    }
    public static String getTopActivityName(Context context){
        String topActivityName=null;
        ActivityManager activityManager = (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(2) ;
        if(runningTaskInfos != null){
            ComponentName f=runningTaskInfos.get(1).baseActivity;
            String topActivityClassName=f.getClassName();
            String temp[]=topActivityClassName.split("\\.");
            topActivityName=temp[temp.length-1];
            System.out.println("topActivityName="+topActivityName);
        }
        return topActivityName;
    }
}


