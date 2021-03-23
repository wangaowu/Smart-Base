package com.bytemiracle.base.framework.fragment.dynamicitem;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bytemiracle.base.R;

import java.lang.ref.WeakReference;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/23 13:33
 */
public class DynamicItemPresenter {

    private final WeakReference<Context> context;
    private final WeakReference<LinearLayout> llContainer;

    public DynamicItemPresenter(LinearLayout llContainer) {
        this.llContainer = new WeakReference<>(llContainer);
        this.context = new WeakReference<Context>(llContainer.getContext());
    }

    public ItemController addItem(ItemData itemData) {
        switch (itemData.itemType) {
            case CONTENT:
                return addContentItem(itemData);
            case CENTER_ICON:
                return addIconItem(itemData);
            case MULTI_EDIT:
                return addMultiEditItem(itemData);
            case RADIO_GROUP:
                return addRadioGroupItem(itemData);
        }
        return addContentItem(itemData);
    }

    private ItemController addRadioGroupItem(ItemData itemData) {
        View itemView = View.inflate(context.get(), R.layout.item_flag_radio_layout, null);
        addItem(itemView);
        TextView tvFlag = itemView.findViewById(R.id.tv_flag);
        RadioGroup radioGroup = itemView.findViewById(R.id.radio_group);
        tvFlag.setText(itemData.flagText);
        ((RadioButton) radioGroup.getChildAt(0)).setText(itemData.radioTexts[0]);
        ((RadioButton) radioGroup.getChildAt(1)).setText(itemData.radioTexts[1]);
        return new ItemController(itemView, itemData.itemType);
    }

    private ItemController addMultiEditItem(ItemData itemData) {
        View itemView = View.inflate(context.get(), R.layout.item_flag_edit_content_layout, null);
        addItem(itemView);
        TextView tvFlag = itemView.findViewById(R.id.tv_flag);
        EditText etContent = itemView.findViewById(R.id.et_content);
        tvFlag.setText(itemData.flagText);
        etContent.setHint(itemData.editHint);
        return new ItemController(itemView, itemData.itemType);
    }

    private ItemController addIconItem(ItemData itemData) {
        View itemView = View.inflate(context.get(), R.layout.item_flag_icon_content_layout, null);
        addItem(itemView);
        TextView tvFlag = itemView.findViewById(R.id.tv_flag);
        ImageView ivCenterIcon = itemView.findViewById(R.id.iv_center_icon);
        Button btnRight = itemView.findViewById(R.id.btn_right);
        tvFlag.setText(itemData.flagText);
        ivCenterIcon.setImageResource(itemData.centerIconResID);
        btnRight.setText(itemData.btnText);
        return new ItemController(itemView, itemData.itemType);
    }

    public ItemController addContentItem(ItemData itemData) {
        View itemView = View.inflate(context.get(), R.layout.item_flag_content_layout, null);
        addItem(itemView);
        TextView tvFlag = itemView.findViewById(R.id.tv_flag);
        EditText etContent = itemView.findViewById(R.id.et_content);
        tvFlag.setText(itemData.flagText);
        etContent.setText(itemData.content);
        return new ItemController(itemView, itemData.itemType);
    }

    public void addItem(View itemView) {
        llContainer.get().addView(itemView, new LinearLayout.LayoutParams(-1, -2));
    }

    public void addItem(View itemView, LinearLayout.LayoutParams layoutParams) {
        llContainer.get().addView(itemView, layoutParams);
    }
}
