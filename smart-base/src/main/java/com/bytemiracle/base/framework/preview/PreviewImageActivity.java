package com.bytemiracle.base.framework.preview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.component.BaseActivity;
import com.bytemiracle.base.framework.view.ShadowLinearLayout;
import com.xuexiang.xui.widget.imageview.photoview.PhotoView;

/**
 * 类功能：预览图片
 *
 * @author gwwang
 * @date 2021/2/5 15:17
 */
public class PreviewImageActivity extends BaseActivity {
    private static final String TAG = "PreviewImageActivity";

    public static final String KEY_IMAGE_TITLE = "image_title";
    public static final String KEY_IMAGE_PATH = "image_path";

    PhotoView photoView;
    ShadowLinearLayout llPage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams layoutParams = llPage.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.width = -1;
        llPage.requestLayout();
    }

    @Override
    protected void initViewsWithSavedInstanceState(Bundle savedInstanceState) {
        photoView = findViewById(R.id.iv_preview);
        llPage = findViewById(R.id.ll_page);
    }

    @Override
    protected String showTitleBar() {
        return "图片";
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTitleText(getIntent().getStringExtra(KEY_IMAGE_TITLE));
        String imagePath = getIntent().getStringExtra(KEY_IMAGE_PATH);
        if (!TextUtils.isEmpty(imagePath)) {
            loadImage(imagePath);
        }
    }

    private void loadImage(String imagePath) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .asBitmap()
                .load(imagePath)
                .apply(options)
                .into(new BitmapImageViewTarget(photoView) {
                });
    }
}
