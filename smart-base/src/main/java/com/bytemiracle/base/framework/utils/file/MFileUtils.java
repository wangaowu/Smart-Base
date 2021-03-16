package com.bytemiracle.base.framework.utils.file;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bytemiracle.base.framework.utils.file.FileUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;

/**
  * @Author: Roman
  * @Description: 文件操作类
  * @Date: 2020/4/22
  **/
public class MFileUtils {
     // 打开系统的文件选择器
     public static void pickActivityFile(Activity activity,int reqCode) {
         Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("*/*");
         intent.addCategory(Intent.CATEGORY_OPENABLE);
         try {
             activity.startActivityForResult( Intent.createChooser(intent, "选择需要上传的文件"), reqCode);
         } catch (android.content.ActivityNotFoundException ex) {
             Toast.makeText(activity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
         }
     }

    // 打开系统的文件选择器
    public static void pickFragmentFile(Fragment fragment, int reqCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            fragment.startActivityForResult( Intent.createChooser(intent, "选择需要上传的文件"), reqCode);
        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(fragment, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    // 选择文件_获取文件路径
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String path = null;
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor == null) {// 未查询到，说明为普通文件，可直接通过URI获取文件路径
                    path = uri.getPath();
                    return path;
                }
                if (cursor.moveToFirst()) {// 多媒体文件，从数据库中获取文件的真实路径
                    String primary = cursor.getString(cursor.getColumnIndex("document_id"));
                    cursor.close();
                    // 字符串截取
                    int strNum = primary.indexOf(":");
                    if(strNum != -1){
                        return primary.substring(strNum+1);
                    }
                    return primary;
                }
            } catch (Exception e) {
                Log.e("MFileUtils", "e:" + e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    // 根据文件路径获取文件名
    public static String getFileNameByPath(String filePath){
         if (filePath == null){
             return "";
         }
        int pos = filePath.lastIndexOf("/");
        if(pos == -1){
            return filePath;
        }else{
            return filePath.substring(pos+1);
        }
    }
    // 获取文件路径
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    // 获取文件后缀名
    public static String getFileSuffixName(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        String fileSuffixName = "";
        if(dotIndex>0){
            fileSuffixName = fileName.substring(dotIndex);
        }
        return fileSuffixName;
    }
    // 获取文档存储路径
    public static String getDocRootPath(Context context){
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        return file.getPath();
    }
     /**
      * @Author: Roman
      * @Description: 调用第三方程序打开文件
      * @Date: 2020/4/23
      * @Demo:
      *     File file = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
      *     File file2 = new File(file+"/1.xls");
      *     MFileUtils.openFile(getContext(),file2.getPath());
      **/
     // 打开文件
     public static void openFile(Context context, String filepath){
         // 判断文件是否存在
         if(!FileUtil.fileIsExists(filepath)){
             Toast.makeText(context, "该文件不存在", Toast.LENGTH_SHORT).show();
             return;
         }
         Intent intent = new Intent(Intent.ACTION_VIEW);
         intent.addCategory(Intent.CATEGORY_DEFAULT);
         File file = new File(filepath);
         Uri uriForFile;
         if (Build.VERSION.SDK_INT > 23){ //Android 7.0之后
             uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
         }else {
             uriForFile = Uri.fromFile(file);
         }
         intent.setAction(Intent.ACTION_VIEW);//动作，查看
         intent.setDataAndType(uriForFile,getMimeTypeFromFile(file));
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//给目标文件临时授权
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         context.startActivity(intent);
     }
     // 使用自定义方法获得文件的MIME类型
     private static String getMimeTypeFromFile(File file) {
         String type = "*/*";
         String fName = file.getName();
         //获取后缀名前的分隔符"."在fName中的位置。
         int dotIndex = fName.lastIndexOf(".");
         if (dotIndex > 0) {
             //获取文件的后缀名
             String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.getDefault());
             //在MIME和文件类型的匹配表中找到对应的MIME类型。
             HashMap<String, String> map = getMimeMap();
             if (!TextUtils.isEmpty(end) && map.keySet().contains(end)) {
                 type = map.get(end);
             }
         }
         return type;
     }
     // 常用"文件扩展名—MIME类型"匹配表。
     public static HashMap<String, String> getMimeMap() {
         HashMap<String, String> mapSimple = new HashMap<>();
         if (mapSimple.size() == 0) {
             mapSimple.put(".3gp", "video/3gpp");
             mapSimple.put(".apk", "application/vnd.android.package-archive");
             mapSimple.put(".asf", "video/x-ms-asf");
             mapSimple.put(".avi", "video/x-msvideo");
             mapSimple.put(".bin", "application/octet-stream");
             mapSimple.put(".bmp", "image/bmp");
             mapSimple.put(".c", "text/plain");
             mapSimple.put(".chm", "application/x-chm");
             mapSimple.put(".class", "application/octet-stream");
             mapSimple.put(".conf", "text/plain");
             mapSimple.put(".cpp", "text/plain");
             mapSimple.put(".doc", "application/msword");
             mapSimple.put(".docx", "application/msword");
             mapSimple.put(".exe", "application/octet-stream");
             mapSimple.put(".gif", "image/gif");
             mapSimple.put(".gtar", "application/x-gtar");
             mapSimple.put(".gz", "application/x-gzip");
             mapSimple.put(".h", "text/plain");
             mapSimple.put(".htm", "text/html");
             mapSimple.put(".html", "text/html");
             mapSimple.put(".jar", "application/java-archive");
             mapSimple.put(".java", "text/plain");
             mapSimple.put(".jpeg", "image/jpeg");
             mapSimple.put(".jpg", "image/jpeg");
             mapSimple.put(".js", "application/x-javascript");
             mapSimple.put(".log", "text/plain");
             mapSimple.put(".m3u", "audio/x-mpegurl");
             mapSimple.put(".m4a", "audio/mp4a-latm");
             mapSimple.put(".m4b", "audio/mp4a-latm");
             mapSimple.put(".m4p", "audio/mp4a-latm");
             mapSimple.put(".m4u", "video/vnd.mpegurl");
             mapSimple.put(".m4v", "video/x-m4v");
             mapSimple.put(".mov", "video/quicktime");
             mapSimple.put(".mp2", "audio/x-mpeg");
             mapSimple.put(".mp3", "audio/x-mpeg");
             mapSimple.put(".mp4", "video/mp4");
             mapSimple.put(".mpc", "application/vnd.mpohun.certificate");
             mapSimple.put(".mpe", "video/mpeg");
             mapSimple.put(".mpeg", "video/mpeg");
             mapSimple.put(".mpg", "video/mpeg");
             mapSimple.put(".mpg4", "video/mp4");
             mapSimple.put(".mpga", "audio/mpeg");
             mapSimple.put(".msg", "application/vnd.ms-outlook");
             mapSimple.put(".ogg", "audio/ogg");
             mapSimple.put(".pdf", "application/pdf");
             mapSimple.put(".png", "image/png");
             mapSimple.put(".pps", "application/vnd.ms-powerpoint");
             mapSimple.put(".ppt", "application/vnd.ms-powerpoint");
             mapSimple.put(".pptx", "application/vnd.ms-powerpoint");
             mapSimple.put(".prop", "text/plain");
             mapSimple.put(".rar", "application/x-rar-compressed");
             mapSimple.put(".rc", "text/plain");
             mapSimple.put(".rmvb", "audio/x-pn-realaudio");
             mapSimple.put(".rtf", "application/rtf");
             mapSimple.put(".sh", "text/plain");
             mapSimple.put(".tar", "application/x-tar");
             mapSimple.put(".tgz", "application/x-compressed");
             mapSimple.put(".txt", "text/plain");
             mapSimple.put(".wav", "audio/x-wav");
             mapSimple.put(".wma", "audio/x-ms-wma");
             mapSimple.put(".wmv", "audio/x-ms-wmv");
             mapSimple.put(".wps", "application/vnd.ms-works");
             mapSimple.put(".xml", "text/plain");
             mapSimple.put(".xls", "application/vnd.ms-excel");
             mapSimple.put(".xlsx", "application/vnd.ms-excel");
             mapSimple.put(".z", "application/x-compress");
             mapSimple.put(".zip", "application/zip");
             mapSimple.put("", "*/*");
         }
         return mapSimple;
     }
}
