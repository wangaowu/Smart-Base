package com.bytemiracle.base.framework.utils.file;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class FileOperationsUtil {
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dictionaryPath 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static void deleteDictionaryInSDCard(String dictionaryPath) {
        if (TextUtils.isEmpty(dictionaryPath)) {
            Log.i("tag", "传入文件夹名称异常");
            return;
        }
        //判断SD卡是否存在
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("tag", "SD卡不存在");
            return;
        }
        File dirFile = new File(dictionaryPath);
        deleteDir(dirFile);
    }

    /**
     * 删除目录下以后缀结尾的文件
     *
     * @param dictionaryPath 将要删除的文件目录
     * @param suffixArr      后缀数组
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static void deleteSuffixEndFile(String dictionaryPath, String[] suffixArr) {
        if (TextUtils.isEmpty(dictionaryPath)) {
            Log.i("tag", "传入文件夹名称异常");
            return;
        }
        //判断SD卡是否存在
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("tag", "SD卡不存在");
            return;
        }
        if (suffixArr == null || suffixArr.length <= 0) {
            Log.i("tag", "后缀数组异常");
            return;
        }
        File dirFile = new File(dictionaryPath);
        if (dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            for (String suffixCurr : suffixArr) {
                for (File tempFile : files) {
                    if (tempFile.getName().endsWith(suffixCurr)) {
                        Log.i("删除：", tempFile.getAbsolutePath());
                        tempFile.delete();
                    }
                }
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除指定后缀的文件
     *
     * @param rootPathFile 根路径
     * @param suffixCurr   后缀
     */
    private static void deleteSuffixFile(File rootPathFile, String suffixCurr) {
    }
}
