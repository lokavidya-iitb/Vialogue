package com.comp.iitb.vialogue.models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ironstein on 19/03/17.
 */

public class PhotoAlbum {

    private Context mContext;

    public long bucketId;
    public String bucketName;
    public String date;
    public String data;
    public int photoCount;
    Cursor mPhotoCursor;
    public PhotoAlbum(Context context) {
        mContext = context;
    }

    public int photoCountByAlbum(String bucketName) {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";
             /*final String[] columns = { MediaStore.Images.Media.DATA,
             MediaStore.Images.Media._ID };*/
             mPhotoCursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                     searchParams, null, orderBy + " DESC");
            if (mPhotoCursor.getCount() > 0) {
               return mPhotoCursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<PhotoAlbum> getAlbumsList(Context context) {
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA
        };

        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = context.getContentResolver().query(images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);
        ArrayList<PhotoAlbum> albumsArrayList = new ArrayList<>();

        PhotoAlbum album;
        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            long bucketId;
            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                bucketId = cur.getInt(bucketIdColumn);
                if (bucket != null && bucket.length() > 0) {
                    album = new PhotoAlbum(context);
                    album.bucketId = bucketId;
                    album.bucketName = bucket;
                    album.date = date;
                    album.data = data;
                    album.photoCount = album.photoCountByAlbum(bucket);
                    albumsArrayList.add(album);

                    // Do something with the values.
                    Log.v("ListingImages", " bucket=" + bucket
                            + "  date_taken=" + date + "  _data=" + data
                            + " bucket_id=" + bucketId);

                }
            } while (cur.moveToNext());

        }
        return albumsArrayList;
    }
}
