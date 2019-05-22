package com.example.camera_beauty.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoManager {
    private final String TAG = PhotoManager.class.getSimpleName();
    private ContentResolver mContentResolver;
    private List<String> mBucketIds;

    private PhotoManager(Context context) {
        mContentResolver = context.getApplicationContext().getContentResolver();
        mBucketIds = new ArrayList<>();
    }

    private static PhotoManager sInstance;

    public static PhotoManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PhotoManager.class) {
                if (sInstance == null) {
                    synchronized (PhotoManager.class) {
                        sInstance = new PhotoManager(context.getApplicationContext());
                    }
                }
            }
        }
        return sInstance;
    }

    public List<Album> getAlbum() {
        mBucketIds.clear();

        List<Album> data = new ArrayList<>();
        String projects[] = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , projects
                , null
                , null
                , MediaStore.Images.Media.DATE_MODIFIED);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Album album = new Album();

                String buckedId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));

                if (mBucketIds.contains(buckedId)) continue;

                mBucketIds.add(buckedId);

                String buckedName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String coverPath = getFrontCoverData(buckedId);

                album.setId(buckedId);
                album.setName(buckedName);
                album.setCoverPath(coverPath);

                data.add(album);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return data;

    }

    public List<Photo> getPhoto(String buckedId) {
        List<Photo> photos = new ArrayList<>();
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED}
                , MediaStore.Images.Media.BUCKET_ID + "=?"
                , new String[]{buckedId}
                , MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Long dataAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                Long dataModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                if (!checkFile(new File(path))) {
                    continue;
                }
                Photo photo = new Photo(path, dataAdded, dataModified);
                photos.add(photo);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return photos;
    }

    public static boolean checkFile(File file) {
        if (!file.exists() || file.length() < 1) {
            return false;
        }
        // 获取该图片的父路径名
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return false;
        }
        return true;
    }

    private String getFrontCoverData(String bucketId) {
        String path = "empty";
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media.BUCKET_ID + "=?", new String[]{bucketId}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    public List<Photo> getAllPhoto() {
        List<Photo> photos = new ArrayList<>();
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED}
                , null
                , null
                , MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Long dataAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                Long dataModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                if (!checkFile(new File(path))) {
                    continue;
                }
                Photo photo = new Photo(path, dataAdded, dataModified);

                photos.add(photo);

            } while (cursor.moveToNext());
            cursor.close();
        }
        return photos;
    }
}
