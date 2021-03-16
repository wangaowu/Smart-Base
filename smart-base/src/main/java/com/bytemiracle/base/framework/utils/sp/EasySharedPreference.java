package com.bytemiracle.base.framework.utils.sp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.StringRes;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SharedPreference 的使用类
 *
 * @author gwwang
 */
public class EasySharedPreference {
    private static final String TAG = "EasySharedPreference";
    public static final String DEFAULT_SP_NAME = "SharedData";

    private static EasySharedPreference self;

    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEditor;

    private static final Set<String> DEFAULT_STRING_SET = new HashSet<>(0);

    private Context mContext;
    private String spName;

    private EasySharedPreference(Application context, String spName) {
        this.mContext = context;
        this.spName = spName;
        this.sSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        this.sEditor = sSharedPreferences.edit();
    }

    public static EasySharedPreference init(Application context, String spName) {
        if (self == null) {
            self = new EasySharedPreference(context, spName);
        }
        return self;
    }

    public static EasySharedPreference get() {
        return self;
    }

    public String getSPName() {
        return spName;
    }

    public EasySharedPreference put(@StringRes int key, Object value) {
        return put(mContext.getString(key), value);
    }

    public EasySharedPreference put(String key, Object value) {

        if (value instanceof String) {
            sEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            sEditor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            sEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            sEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            sEditor.putLong(key, (Long) value);
        } else if (null != value) {
            sEditor.putString(key, value.toString());
        }
        sEditor.apply();
        return self;
    }

    public Object get(@StringRes int key, Object defaultObject) {
        return get(mContext.getString(key), defaultObject);
    }

    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sSharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sSharedPreferences.getInt(key, (int) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sSharedPreferences.getBoolean(key, (boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sSharedPreferences.getFloat(key, (float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sSharedPreferences.getLong(key, (long) defaultObject);
        }
        return null;
    }

    public EasySharedPreference putInt(String key, int value) {
        sEditor.putInt(key, value);
        sEditor.apply();
        return this;
    }

    public EasySharedPreference putInt(@StringRes int key, int value) {
        return putInt(mContext.getString(key), value);
    }

    public int getInt(@StringRes int key) {
        return getInt(mContext.getString(key));
    }

    public int getInt(@StringRes int key, int defValue) {
        return getInt(mContext.getString(key), defValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }


    public int getInt(String key, int defValue) {
        return sSharedPreferences.getInt(key, defValue);
    }

    public EasySharedPreference putFloat(@StringRes int key, float value) {
        return putFloat(mContext.getString(key), value);
    }

    public EasySharedPreference putFloat(String key, float value) {
        sEditor.putFloat(key, value);
        sEditor.apply();
        return self;
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defValue) {
        return sSharedPreferences.getFloat(key, defValue);
    }

    public float getFloat(@StringRes int key) {
        return getFloat(mContext.getString(key));
    }

    public float getFloat(@StringRes int key, float defValue) {
        return getFloat(mContext.getString(key), defValue);
    }

    public EasySharedPreference putLong(@StringRes int key, long value) {
        return putLong(mContext.getString(key), value);
    }

    public EasySharedPreference putLong(String key, long value) {
        sEditor.putLong(key, value);
        sEditor.apply();
        return self;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defValue) {
        return sSharedPreferences.getLong(key, defValue);
    }

    public long getLong(@StringRes int key) {
        return getLong(mContext.getString(key));
    }

    public long getLong(@StringRes int key, long defValue) {
        return getLong(mContext.getString(key), defValue);
    }

    public EasySharedPreference putString(@StringRes int key, String value) {
        return putString(mContext.getString(key), value);
    }

    public EasySharedPreference putString(String key, String value) {
        sEditor.putString(key, value);
        sEditor.apply();
        return self;
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defValue) {
        return sSharedPreferences.getString(key, defValue);
    }

    public String getString(@StringRes int key) {
        return getString(mContext.getString(key), "");
    }

    public String getString(@StringRes int key, String defValue) {
        return getString(mContext.getString(key), defValue);
    }

    public EasySharedPreference putBoolean(@StringRes int key, boolean value) {
        return putBoolean(mContext.getString(key), value);
    }

    public EasySharedPreference putBoolean(String key, boolean value) {
        sEditor.putBoolean(key, value);
        sEditor.apply();
        return self;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sSharedPreferences.getBoolean(key, defValue);
    }

    public boolean getBoolean(@StringRes int key) {
        return getBoolean(mContext.getString(key));
    }

    public boolean getBoolean(@StringRes int key, boolean defValue) {
        return getBoolean(mContext.getString(key), defValue);
    }

    public EasySharedPreference putStringSet(@StringRes int key, Set<String> value) {
        return putStringSet(mContext.getString(key), value);
    }

    public EasySharedPreference putStringSet(String key, Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sEditor.putStringSet(key, value);
            sEditor.apply();
        }
        return self;
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, DEFAULT_STRING_SET);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sSharedPreferences.getStringSet(key, defValue);
        } else {
            return DEFAULT_STRING_SET;
        }
    }

    public Set<String> getStringSet(@StringRes int key) {
        return getStringSet(mContext.getString(key));
    }

    public Set<String> getStringSet(@StringRes int key, Set<String> defValue) {
        return getStringSet(mContext.getString(key), defValue);
    }


    public boolean contains(String key) {
        return sSharedPreferences.contains(key);
    }

    public boolean contains(@StringRes int key) {
        return contains(mContext.getString(key));
    }

    public Map<String, ?> getAll() {
        return sSharedPreferences.getAll();
    }

    public EasySharedPreference remove(@StringRes int key) {
        return remove(mContext.getString(key));
    }

    public EasySharedPreference remove(String key) {
        sEditor.remove(key);
        sEditor.apply();
        return self;
    }

    public EasySharedPreference clear() {
        sEditor.clear();
        sEditor.apply();
        return self;
    }

    /**
     * 获取对象
     *
     * @param key
     * @param clazz 类型
     * @param <T>
     * @return
     */
    public <T extends Serializable> T getObject(String key, Class<T> clazz) {
        String serializableContent = getString(key);
        try {
            Object o = SerializeUtils.serializeToObject(serializableContent);
            return (T) o;
        } catch (Exception e) {
            Log.e(TAG, "getObject: [" + clazz.getClass() + "], 失败," + e.getCause());
            return null;
        }
    }

    /**
     * put对象
     *
     * @param key
     * @param serializableObject 类型
     * @return
     */
    public void putObject(String key, Object serializableObject) {
        try {
            putString(key, SerializeUtils.serialize(serializableObject));
        } catch (IOException e) {
            Log.e(TAG, "putObject: [" + serializableObject.getClass() + "], 失败," + e.getCause());
        }
    }

}