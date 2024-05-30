package com.site.springboot.core.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheSet {

    /**
     * 缓存key
     */
    String key() default "";

    /**
     * 返回值类型，集合或单体
     * single:单体
     * list:集合
     */
    String retype() default "single";

}
