package com.bytemiracle.base.framework.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.listener.CommonAsync4Listener;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.bytemiracle.base.framework.utils.common.ListUtils;
import com.bytemiracle.base.framework.utils.app.SoftKeyboardUtils;
import com.bytemiracle.base.framework.view.selectWindow.AppPopupSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：应用内搜索封装
 *
 * @author gwwang
 * @date 2021/2/23 8:47
 */
public class AppSearchView extends FrameLayout {
    private EditText etContent;
    private View ivClear;
    private boolean visibleClear;
    private String hintString;
    private boolean visiblePopList;
    private CommonAsync4Listener<List<String>> popInitListener;
    private CommonAsyncListener<String> searchListener;
    private AppPopupSelector appPopupSelector;
    private View contentView;

    /**
     * 拦截键盘输入删除事件
     */
    private boolean isBlockKeyboardTextChangedEvent = false;

    public AppSearchView(@NonNull Context context) {
        this(context, null);
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
        initViews();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.search_view);
        visibleClear = a.getBoolean(R.styleable.search_view_visible_clear, false);
        hintString = a.getString(R.styleable.search_view_hint_text);
        visiblePopList = a.getBoolean(R.styleable.search_view_visible_poplist, false);
        a.recycle();
    }

    private void initViews() {
        contentView = View.inflate(getContext(), R.layout.search_view_layout, null);
        etContent = contentView.findViewById(R.id.et_content);
        ivClear = contentView.findViewById(R.id.iv_clear);
        addView(contentView, new ViewGroup.LayoutParams(-1, -1));

        //设置搜索的hint
        etContent.setHint(TextUtils.isEmpty(hintString) ? "搜索" : hintString);
        //设置背景（搜索联想时背景使用小圆角）
        contentView.setBackgroundResource(visiblePopList ? R.drawable.bg_app_common_input_radius_5_white_rectangle
                : R.drawable.bg_app_common_input_radius_20_white_rectangle);
        //如果设置了联想，关闭直接搜索功能
        if (visiblePopList) {
            etContent.setImeOptions(EditorInfo.IME_ACTION_PREVIOUS);
        }
    }

    private void visibleClear(int visible) {
        if (visibleClear) {
            ivClear.setVisibility(visible);
        }
    }

    private void clearText() {
        etContent.setText("");
    }

    /**
     * 设置搜索的事件
     *
     * @param searchListener
     */
    public void setSearchListener(CommonAsyncListener<String> searchListener) {
        setSearchAndClearListener(searchListener, null);
    }

    public AppSearchView initPopListener(CommonAsync4Listener<List<String>> popInitListener) {
        this.popInitListener = popInitListener;
        return this;
    }

    /**
     * 在200ms内不响应键盘的textChange事件（）
     */
    private void delayUnlockKeyboardTextChangedEvent() {
        postDelayed(() -> {
            isBlockKeyboardTextChangedEvent = false;
        }, 200);
    }

    /**
     * 设置搜索，和清空的事件
     *
     * @param searchListener
     * @param clearListener
     */
    public void setSearchAndClearListener(CommonAsyncListener<String> searchListener, CommonAsyncListener<Object> clearListener) {
        this.searchListener = searchListener;
        this.etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    isBlockKeyboardTextChangedEvent = true;
                    delayUnlockKeyboardTextChangedEvent();
                    Editable text = etContent.getText();
                    if (searchListener != null && !TextUtils.isEmpty(text)) {
                        searchListener.doSomething(text.toString());
                    } else if (clearListener != null && (text == null || TextUtils.isEmpty(text.toString()))) {
                        clearListener.doSomething(null);
                    }
                    dismissPopSelector();
                    SoftKeyboardUtils.hideSoftInput(etContent);
                    return true;
                }
                return false;
            }
        });
        this.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dismissPopSelector();
                if (isBlockKeyboardTextChangedEvent) {
                    return;
                }
                if (!TextUtils.isEmpty(s)) {
                    visibleClear(VISIBLE);
                    if (!visiblePopList) {
                        searchListener.doSomething(s.toString());
                    }
                    showPopList(s.toString());
                } else if (clearListener != null && before == 1) {
                    //表示键盘逐步删除(搜索框清空按钮因minLenght>=2，不会走进该监听)
                    clearListener.doSomething(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivClear.setOnClickListener(v -> {
            isBlockKeyboardTextChangedEvent = true;
            delayUnlockKeyboardTextChangedEvent();
            clearText();
            visibleClear(GONE);
            if (clearListener != null) {
                clearListener.doSomething(null);
            }
        });
    }

    private void dismissPopSelector() {
        if (appPopupSelector != null) {
            appPopupSelector.dismissWindow();
        }
    }

    private void showPopList(String inputContent) {
        if (visiblePopList && popInitListener != null) {
            List<String> popDataList = popInitListener.getInitData();
            List<String> containsKeyList = findContainsKeyList(inputContent, popDataList);
            if (!ListUtils.isEmpty(containsKeyList)) {
                int itemHeight = getResources().getDimensionPixelSize(R.dimen.dpx_50);
                appPopupSelector = AppPopupSelector.newInstance(this, false,
                        itemHeight, R.layout.item_app_popup_selector_height_50_layout);
                appPopupSelector.bindSelector(containsKeyList, new CommonAsyncListener<Integer>() {
                    @Override
                    public void doSomething(Integer index) {
                        searchListener.doSomething(containsKeyList.get(index));
                        clearText();
                        etContent.clearFocus();
                        SoftKeyboardUtils.hideSoftInput(etContent);
                    }
                });
                appPopupSelector.showAsTop();
            }
        }
    }

    private List<String> findContainsKeyList(String key, List<String> popDataList) {
        List<String> strings = new ArrayList<>();
        for (String s : popDataList) {
            if (s.contains(key)) {
                strings.add(s);
            }
        }
        return strings;
    }

}
