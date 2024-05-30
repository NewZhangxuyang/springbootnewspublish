package com.site.springboot.core.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheGet {

    /**
     * 过期时间，默认为6000秒
     */
    long expireTime() default 6000;

    /**
     * key,若缓存中没有，则从数据库中获取，并放入缓存
     */
    String key() default "";

    /**
     * 返回值类型，集合或单体
     * single:单体
     * list:集合
     */
    String retype() default "single";


}
