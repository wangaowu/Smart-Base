package com.bytemiracle.base.framework.update;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.component.BaseActivity;
import com.bytemiracle.base.framework.http.SmartDownloadRequest;
import com.bytemiracle.base.framework.utils.file.FileUtil;
import com.bytemiracle.base.framework.utils.XToastUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/10 11:32
 */
public class UpdateDialogController {
    private static final int DIALOG_MAX_PROGRESS = 100;

    private MaterialDialog dialog;
    private Button btnUpdate;
    private Button btnBackgroundUpdate;
    private NumberProgressBar numberProgressBar;
    private WeakReference<BaseActivity> activity;

    public UpdateDialogController(BaseActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    /**
     * 创建dialog
     *
     * @param updateResponse
     * @return
     */
    public MaterialDialog createDialog(UpdateResponse updateResponse) {
        dialog = new MaterialDialog.Builder(activity.get())
                .customView(R.layout.dialog_update_version, true)
                .cancelable(false)
                .build();
        initViews(dialog, updateResponse);
        return dialog;
    }

    /**
     * 显示dialog
     */
    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void initViews(MaterialDialog dialog, UpdateResponse updateResponse) {
        TextView tvVersionDeclare = dialog.findViewById(R.id.tv_version_declare);
        TextView tvVersionName = dialog.findViewById(R.id.tv_version_name);
        TextView tvVersionSize = dialog.findViewById(R.id.tv_version_size);
        btnUpdate = dialog.findViewById(R.id.btn_update);
        btnBackgroundUpdate = dialog.findViewById(R.id.btn_background_update);
        numberProgressBar = dialog.findViewById(R.id.number_progress_bar);

        //初始，仅显示更新按钮
        btnUpdate.setVisibility(View.VISIBLE);
        numberProgressBar.setVisibility(View.GONE);
        btnBackgroundUpdate.setVisibility(View.GONE);

        btnUpdate.setOnClickListener(v -> {
            numberProgressBar.setVisibility(View.VISIBLE);
            btnBackgroundUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            //启动下载
            prepareDownload(updateResponse);
        });
        btnBackgroundUpdate.setOnClickListener(v -> {
            //因下载已在服务中，此处仅隐藏弹窗
            dialog.dismiss();
        });
        //设置数据
        tvVersionName.setText(updateResponse.getData().getVersionName());
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        tvVersionSize.setText(nf.format(Double.parseDouble(updateResponse.getData().getApkSize()) * .001f) + "M");
        tvVersionDeclare.setText(updateResponse.getData().getModifyContent());
    }

    private void prepareDownload(UpdateResponse updateResponse) {
        String apkServerUrl = updateResponse.getData().getDownloadUrl();
        String apkName = FileUtil.getFileDisplayName(apkServerUrl);
        if (DownLoadManager.getInstance().isExist(apkName)) {
            //文件已经存在,安装即可
            XToastUtils.info("可安装包已存在");
            numberProgressBar.setVisibility(View.GONE);
            btnBackgroundUpdate.setText("安装");
            btnBackgroundUpdate.setOnClickListener(v -> {
                DownLoadManager.getInstance().installApk(apkName);
            });
            return;
        }
        //文件不存在，启动后端服务下载
        numberProgressBar.setMax(DIALOG_MAX_PROGRESS);
        DownLoadManager.getInstance().downloadFileOnService(apkServerUrl,
                UpdateComponent.DOWNLOAD_DIR,
                new SmartDownloadRequest.OnDownloadChangeListener() {
                    @Override
                    public void onProgress(float fraction) {
                        //更新进度条
                        if (dialog != null && dialog.isShowing()) {
                            numberProgressBar.setProgress((int) (fraction * DIALOG_MAX_PROGRESS));
                        }
                    }

                    @Override
                    public void onSuccess(File file) {
                        //下载完成
                        if (dialog != null && dialog.isShowing()) {
                            numberProgressBar.setVisibility(View.GONE);
                            btnBackgroundUpdate.setText("安装");
                            btnBackgroundUpdate.setOnClickListener(v -> {
                                DownLoadManager.getInstance().installApk(apkName);
                            });
                        } else {
                            new UpdateDialogController(activity.get()).createDialog(updateResponse).show();
                        }
                    }

                    @Override
                    public void onFailed(String reason) {
                        //下载失败
                        if (dialog != null && dialog.isShowing()) {
                            btnUpdate.setVisibility(View.VISIBLE);
                            numberProgressBar.setVisibility(View.GONE);
                            btnBackgroundUpdate.setVisibility(View.GONE);
                        }
                    }
                });

    }
}
