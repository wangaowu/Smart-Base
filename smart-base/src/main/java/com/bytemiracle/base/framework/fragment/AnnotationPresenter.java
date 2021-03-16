package com.bytemiracle.base.framework.fragment;

import android.util.Log;

import com.bytemiracle.base.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类功能：fragment的注解处理类
 *
 * @author gwwang
 * @date 2021/1/8 9:02
 */
public class AnnotationPresenter {
    private static final String TAG = "AnnotationPresenter";

    private Class clazz;

    /**
     * 构造方法
     *
     * @param Clazz
     */
    public AnnotationPresenter(Class Clazz) {
        this.clazz = Clazz;
    }

    /**
     * 查找定义fragment的tag内容
     *
     * @return fragment的tag内容
     */
    public String findDefinedFragmentTag() {
        Annotation annotation = getAnnotation(FragmentTag.class);
        if (annotation instanceof FragmentTag) {
            return ((FragmentTag) annotation).name();
        }
        //注解tag为空
        Log.e(TAG, clazz.getSimpleName() + "'s Annotation Tag is null , switch error");
        return "";
    }

    /**
     * 查找定义fragment的anim信息
     *
     * @return fragment的anim信息
     */
    public int[] findDefinedFragmentAnim() {
        Annotation annotation = getAnnotation(FragmentTag.class);
        if (annotation instanceof FragmentTag) {
            boolean useSlide = ((FragmentTag) annotation).useSlide();
            if (useSlide) {
                return new int[]{R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left};
            }
        }
        //注解tag为空
        return null;
    }

    private Annotation getAnnotation(Class<? extends Object> annotationClazz) {
        return clazz.getAnnotation(annotationClazz);
    }

    /**
     * 构建对象
     *
     * @param initType  构造参数类型
     * @param initParam 构造参数值
     * @param <T>       invoke什么类型的结果
     * @return
     */
    public <T> T newInstanceObject(Class initType, Object initParam) {
        try {
            Constructor<?> constructor = clazz.getConstructor(initType);
            return (T) constructor.newInstance(initParam);
        } catch (Exception e) {
            Log.e(TAG, "newInstanceObject() error: " + e.toString());
            return null;
        }
    }

    /**
     * 设置私有字段的值
     *
     * @param tabObject     修改对象
     * @param fieldName     字段名称
     * @param fieldNewValue 字段值
     */
    public void setPrivateFieldValue(Object tabObject, String fieldName, Object fieldNewValue) {
        try {
            Field field = tabObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(tabObject, fieldNewValue);
        } catch (Exception e) {
            Log.e(TAG, "set private field value failed: " + e.toString());
        }
    }

    /**
     * 获取私有字段的值
     *
     * @param tabObject 操作对象
     * @param fieldName 字段名称
     */
    public <T> T getPrivateFieldValue(Object tabObject, String fieldName) {
        try {
            Field field = tabObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(tabObject);
        } catch (Exception e) {
            Log.e(TAG, "set private field value failed: " + e.toString());
            return null;
        }
    }

    /**
     * 获取泛型
     *
     * @return
     */
    public Type getGenType() {
        Type genType = clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genType).getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return actualTypeArguments[0];
            }
        }
        return Object.class;
    }
}
