package com.bytemiracle.base.framework.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtil {

    private static final String TAG = "FileUtil";

    private static final int BUFFER = 8192;

    public static List<String> fileExtensionList = Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "pdf");

    /**
     * @param filePath f.mkdir(); //只能创建一级目录
     *                 f.mkdirs() //能创建多级目录
     * @return
     * @Description: 检查目录是否存在, 不存在则创建
     */

    public static boolean checkDir(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }


    /**
     * 检查文件的目录是否存在，不存在则创建
     *
     * @param filePath
     * @return
     */
    public static boolean checkFile(String filePath) {
        File f = new File(filePath);
        return checkDir(f.getParent());
    }

    /**
     * @return
     * @Description: 判断SD卡是否存在, 并且是否具有读写权限
     */
    public static boolean isExistSD() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    /**
     * Java文件操作 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 获取文件展示名称
     *
     * @return
     */
    public static String getFileDisplayName(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            int index = filePath.lastIndexOf('/');
            if (index > -1 && (index < (filePath.length() - 1))) {
                return filePath.substring(index + 1);
            }
        }
        return filePath;
    }

    // 判断文件是否存在
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 获取缓存路径
     */
    public static String getCacheDir(Context context) {
        String cacheDir;
        if (FileUtil.isExistSD()) {
            //SD存在
            cacheDir = FileUtil.getSDCacheDir(context);
        } else {
            //不存在则使用系统目录
            cacheDir = context.getCacheDir().getPath();
        }
        cacheDir += "/";
        //L.e("----缓存目录---->>>:" + cacheDir);

//		checkDir(cacheDir);
        return cacheDir;
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        if (isExistSD()) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        }
        return null;
    }

    /**
     * @param context
     * @return
     * @Description: 获取当前应用SD卡缓存目录
     */
    public static String getSDCacheDir(Context context) {
        //api大于8的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            //目录为/mnt/sdcard/Android/data/com.mt.mtpp/cache
            return context.getExternalCacheDir().getPath();
        }
        String cacheDir = "/Android/data/" + context.getPackageName() + "/cache";
        return Environment.getExternalStorageDirectory().getPath() + cacheDir;

//		return getCacheDirectory(context,true);
    }


    /**
     * 将字节缓冲区按照固定大小进行分割成数组
     *
     * @param buffer 缓冲区
     * @param length 缓冲区大小
     * @param spsize 切割块大小
     * @return
     */
    public static ArrayList<byte[]> splitBuffer(byte[] buffer, int length, int spsize) {
        ArrayList<byte[]> array = new ArrayList<byte[]>();
        if (spsize <= 0 || length <= 0 || buffer == null || buffer.length < length)
            return array;
        int size = 0;
        while (size < length) {
            int left = length - size;
            if (spsize < left) {
                byte[] sdata = new byte[spsize];
                System.arraycopy(buffer, size, sdata, 0, spsize);
                array.add(sdata);
                size += spsize;
            } else {
                byte[] sdata = new byte[left];
                System.arraycopy(buffer, size, sdata, 0, left);
                array.add(sdata);
                size += left;
            }
        }
        return array;
    }

    /**
     * 读取asset目录下音频文件。
     *
     * @return 二进制文件数据
     */
    public static byte[] readAudioFile(String path) {
        try {
            InputStream ins = new FileInputStream(new File(path));

            int count = 0;
            while (count == 0) {
                count = ins.available();
            }
            byte[] b = new byte[count];
            ins.read(b);

			/*byte[] data = new byte[ins.available()];
			ins.read(data);*/
            ins.close();
            return b;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getNowString(final java.text.DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    public static String millis2String(final long millis, final java.text.DateFormat format) {
        return format.format(new Date(millis));
    }


    /**
     * 请求网络图片
     *
     * @param imgUrl
     * @param asyncViewListener
     */
    public static void setNetWorkImg(String imgUrl, CommonAsyncListener<Drawable> asyncViewListener) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = (InputStream) new URL(imgUrl).getContent();
                        final Drawable d = Drawable.createFromStream(is, "src");
                        is.close();
                        XUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                asyncViewListener.doSomething(d);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            System.out.println("设置图片异常");
            e.printStackTrace();
        }
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param serverFileUrl 输入文件url
     * @param downLoadFile  下载到的路径
     * @return
     * @throws Exception
     */
    public static void download(String serverFileUrl, String downLoadFile, CommonAsyncListener<Object> downloadCompleteLister) {                           /***加载正文***/
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(serverFileUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("moer", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                File pathFile = new File(downLoadFile);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                File file = new File(downLoadFile);
                if (!file.exists()) {
                    file.mkdir();
                }
                if (response != null) {
                    InputStream is = response.body().byteStream();
                    FileOutputStream fos = new FileOutputStream(file);
                    int len = 0;
                    byte[] buffer = new byte[2048];
                    while (-1 != (len = is.read(buffer))) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                }
                downloadCompleteLister.doSomething(null);
            }
        });
    }

    private static final String FILE_TEMP_FLAG = "_temp";

    public static String appendTempFlag(String filePath) {
        return filePath + FILE_TEMP_FLAG;
    }

    public static String removeTempFlag(String filePath) {
        if (filePath.contains(FILE_TEMP_FLAG)) {
            File srcFile = new File(filePath);
            filePath = filePath.replace(FILE_TEMP_FLAG, "");
            File newFile = new File(filePath);
            srcFile.renameTo(newFile);
        }
        return filePath;
    }

    /**
     * 清空dir
     * 也不用递归删除了，使用全部删除后新建该文件夹
     *
     * @param dir
     */
    public static void clearDirFiles(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }
}
