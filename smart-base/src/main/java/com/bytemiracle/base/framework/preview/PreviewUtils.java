package com.bytemiracle.base.framework.preview;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.component.BaseActivity;
import com.bytemiracle.base.framework.utils.IMConfig;
import com.bytemiracle.base.framework.utils.file.FileUtil;
import com.bytemiracle.base.framework.utils.file.MFileUtils;
import com.bytemiracle.base.framework.utils.XToastUtils;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.xuexiang.xutil.XUtil;

import java.io.File;

/**
 * 类功能：预览工具
 *
 * @author gwwang
 * @date 2021/2/5 14:45
 */
public class PreviewUtils {

    /**
     * 打开界面
     *
     * @param activity
     * @param clazz
     */
    public static void openActivity(Activity activity, Class<? extends BaseActivity> clazz) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                activity,
                R.anim.push_fall_out,
                R.anim.anim_stay);
        ActivityCompat.startActivity(activity, new Intent(activity, clazz), compat.toBundle());
    }

    /**
     * 预览图片
     *
     * @param activity
     * @param imagePath
     * @param title
     */
    public static void viewImage(Activity activity, String imagePath, String title) {
        //打开预览界面
        Intent intent = new Intent(activity, PreviewImageActivity.class);
        intent.putExtra(PreviewImageActivity.KEY_IMAGE_PATH, imagePath);
        intent.putExtra(PreviewImageActivity.KEY_IMAGE_TITLE, title);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                activity,
                R.anim.push_fall_out,
                R.anim.anim_stay);
        ActivityCompat.startActivity(activity, intent, compat.toBundle());
    }

    /**
     * 预览文件
     *
     * @param serverUrl    文件服务器地址
     * @param localDirPath 本地地址
     * @param fileName     文件名
     */
    public static void viewFile(String serverUrl, String localDirPath, String fileName) {
        String fileType = FileUtil.getExtensionName(serverUrl);
        if (serverUrl.contains(IMConfig.IM_SERVER_HOST)) {
            String downloadString = localDirPath + File.separator + fileName;
            File downloadFile = new File(downloadString);
            if (FileUtil.fileExtensionList.contains(fileType)) {
                //远端文件需要下载
                if (!FileUtil.fileIsExists(serverUrl)) {
                    //下载文件之前先判断对应文件夹底下是否有同名文件
                    if (downloadFile.exists() && downloadFile.length() > 0) {
                        MFileUtils.openFile(XUtil.getContext(), downloadString);
                    } else {
                        //下载文件并打开
                        FileUtil.download(serverUrl,
                                downloadString,
                                data -> MFileUtils.openFile(XUtil.getContext(), downloadString));
                    }
                }
            } else {
                //直接下载文件即可
                if (!downloadFile.exists()) {
                    FileUtil.download(serverUrl,
                            downloadString,
                            data -> XToastUtils.success("文件下载成功"));
                }
            }
        } else {
            //本地发送文件，不需要下载
            if (FileUtil.fileExtensionList.contains(fileType)) {
                MFileUtils.openFile(XUtil.getContext(), serverUrl);
            }
        }
    }


    /**
     * 获取图片选择的配置
     *
     * @param fragment
     * @param maxSelectNum
     * @return
     */
    public static PictureSelectionModel getPictureSelector(Fragment fragment, int maxSelectNum) {
        String compressPath = fragment.getContext().getCacheDir().getPath();
        return PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .compress(true)
                .compressQuality(40)
                .compressSavePath(compressPath)
                .maxSelectNum(maxSelectNum)
                .minSelectNum(1)
                .theme(R.style.picture_white_style);
    }
}