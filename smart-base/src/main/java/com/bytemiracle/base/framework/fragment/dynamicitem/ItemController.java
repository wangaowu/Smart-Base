package com.bytemiracle.base.framework.fragment.dynamicitem;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bytemiracle.base.R;

import java.lang.ref.WeakReference;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/23 15:41
 */
public class ItemController {
    private WeakReference<View> itemView;
    private ItemData.ItemType itemType;

    public ItemController(View itemView, ItemData.ItemType itemType) {
        this.itemView = new WeakReference<>(itemView);
        this.itemType = itemType;
    }

    private void lockEditText(EditText editText, boolean locked) {
        editText.setFocusable(!locked);
        editText.setClickable(!locked);
    }

    private void lockView(View view, boolean locked) {
        view.setClickable(!locked);
    }

    private void lockRadioGroup(RadioGroup radioGroup, boolean locked) {
        ((RadioButton) radioGroup.getChildAt(0)).setClickable(!locked);
        ((RadioButton) radioGroup.getChildAt(1)).setClickable(!locked);
    }

    /**
     * 解锁条目
     */
    public void unlockItem() {
        lockItem(false);
    }

    /**
     * 锁定条目
     */
    public void lockItem() {
        lockItem(true);
    }

    private void lockItem(boolean locked) {
        switch (itemType) {
            case CONTENT:
            case MULTI_EDIT:
                lockEditText(itemView.get().findViewById(R.id.et_content), locked);
                break;
            case CENTER_ICON:
                lockView(itemView.get().findViewById(R.id.iv_center_icon), locked);
                lockView(itemView.get().findViewById(R.id.btn_right), locked);
                break;
            case RADIO_GROUP:
                lockRadioGroup(itemView.get().findViewById(R.id.radio_group), locked);
                break;
        }
    }
}
