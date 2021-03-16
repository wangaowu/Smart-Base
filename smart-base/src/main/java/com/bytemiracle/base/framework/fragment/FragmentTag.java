package com.bytemiracle.base.framework.fragment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类功能：Fragment类的注解
 *
 * @author gwwang
 * @date 2021/2/24 11:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FragmentTag {
    /**
     * @return 页面的名称
     */
    String name() default "";

    boolean useSlide() default false;
}
