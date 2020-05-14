package com.example.lt.timeset_andorid.util;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
public class ReadUtils {

    public static final String TAG = "ReaderUtils";

    /**
     * 查询内容解析器，找到文件存储地址
     * <p>ef: android中转换content://media/external/images/media/539163为/storage/emulated/0/DCIM/Camera/IMG_20160807_123123.jpg
     * <p>把content://media/external/images/media/X转换为file:///storage/sdcard0/Pictures/X.jpg
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            Log.i(TAG, "getRealPathFromUri: " + contentUri);
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.getColumnCount() > 0) {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                Log.i(TAG, "getRealPathFromUri: column_index=" + column_index + ", path=" + path);
                return path;
            } else {
                Log.w(TAG, "getRealPathFromUri: invalid cursor=" + cursor + ", contentUri=" + contentUri);
            }
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromUri failed: " + e  + ", contentUri=" + contentUri, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }
    /**
     * 获取完整文件名(包含扩展名)
     * @param filePath
     * @return
     */
    public static String getFilenameWithExtension(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return "";
        }
        int lastIndex = filePath.lastIndexOf(File.separator);
        String filename = filePath.substring(lastIndex + 1);
        return filename;
    }

    /**
     * 判断文件路径的文件名是否存在文件扩展名 eg: /external/images/media/2283
     * @param filePath
     * @return
     */
    public static boolean isFilePathWithExtension(String filePath) {
        String filename = getFilenameWithExtension(filePath);
        return filename.contains(".");
    }
}
