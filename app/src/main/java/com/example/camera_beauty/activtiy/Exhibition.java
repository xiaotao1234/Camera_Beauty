package com.example.camera_beauty.activtiy;

import android.content.ComponentName;
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
import android.widget.ListView;
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
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            filename.add(new String(data, 0, data.length - 1));
        }
        recyclerView = findViewById(R.id.exhibition_list);
        moreimageview = findViewById(R.id.more);
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
            checkid = false;
        } else {
            super.onBackPressed();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
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
}


