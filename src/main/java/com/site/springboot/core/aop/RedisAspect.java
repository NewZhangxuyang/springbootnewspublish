package com.site.springboot.core.aop;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.springboot.core.entity.News;
import com.site.springboot.core.util.RedisUtil;
import com.site.springboot.core.util.annotation.RedisCacheDel;
import com.site.springboot.core.util.annotation.RedisCacheGet;
import com.site.springboot.core.util.annotation.RedisCacheSet;
import jakarta.annotation.Resource;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Aspect
@Slf4j
public class RedisAspect {


    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Resource
    private RedisUtil redis;

    @Pointcut("@annotation(com.site.springboot.core.util.annotation.RedisCacheSet)")
    public void redisCacheSetPointcut() {
    }

    @Pointcut("@annotation(com.site.springboot.core.util.annotation.RedisCacheGet)")
    public void redisCacheGetPointcut() {
    }

    @Pointcut("@annotation(com.site.springboot.core.util.annotation.RedisCacheDel)")
    public void redisCacheDelPointcut() {
    }


    @Around("redisCacheGetPointcut()")
    public Object aroundRedisCache(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        RedisCacheGet redisCache = method.getAnnotation(RedisCacheGet.class);
        long expireTime = redisCache.expireTime();
        String retype = redisCache.retype();

        if (retype.equals("list")) {
            Long[] ids = (Long[]) args[0];
            List list = Arrays.asList(ids); //id 参数列表
            List<News> returnList = new ArrayList<>();//缓存中含有的对象
            list.forEach(id -> {
                if (redis.hasKey(id + "redis_ZhangXuYang")) {
                    News news = (News) redis.get(id + "redis_ZhangXuYang");
                    returnList.add(news);
                }
            });
            if (returnList.size() == list.size()) {
                return returnList;
            }
            Object proceed = joinPoint.proceed();
            List ret = (List) proceed;
            ret.forEach(item -> {
                News newItem = (News) item;
                redis.set(newItem.getNewsId() + "redis_ZhangXuYang", newItem);
            });
            return proceed;
        }
        Long id = (Long) args[0];
        if (redis.hasKey(id + "redis_ZhangXuYang")) {
            News news = (News) redis.get(id + "redis_ZhangXuYang");
            return news;
        }
        Object proceed = joinPoint.proceed();
        News news = (News) proceed;
        redis.set(news.getNewsId() + "redis_ZhangXuYang", news);
        return proceed;
    }


    @Around("redisCacheSetPointcut()")
    public Object aroundRedisCacheSet(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        RedisCacheSet redisCache = method.getAnnotation(RedisCacheSet.class);
        Object proceed = joinPoint.proceed();
        News news = (News) proceed;
        String key = news.getNewsId().toString();
        String redisKey = key + "redis_ZhangXuYang";
        if (redisCache.retype().equals("single")) {
            redis.set(redisKey, proceed);
        } else {
            List list = (List) proceed;
            list.forEach(item -> {
                News newItem = (News) item;
                redis.set(newItem.getNewsId() + "redis_ZhangXuYang", newItem);
            });
        }
        return proceed;
    }

    @Around("redisCacheDelPointcut()")
    public Object aroundRedisCacheDel(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        RedisCacheDel redisCache = method.getAnnotation(RedisCacheDel.class);
        Object proceed = joinPoint.proceed();
        if (redisCache.retype().equals("single")) {
            News news = (News) proceed;
            String key = news.getNewsId().toString();
            String redisKey = key + "redis_ZhangXuYang";
            if (redis.hasKey(redisKey)) {
                redis.del(redisKey);
            }
        } else {
            List list = (List) proceed;
            list.forEach(item -> {
                News news = (News) item;
                redis.del(news.getNewsId() + "redis_ZhangXuYang");
            });
        }
        return proceed;
    }
}
