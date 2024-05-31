package com.site.springboot.core;

import com.site.springboot.core.dao.ContentIndexMapper;
import com.site.springboot.core.dao.NewsMapper;
import com.site.springboot.core.entity.News;
import com.site.springboot.core.entity.NewsIndex;
import com.site.springboot.core.service.NewsIndexService;
import com.site.springboot.core.service.NewsService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Application.class)
public class SpringTestApplication {


    private static final Logger log = LoggerFactory.getLogger(SpringTestApplication.class);
    @Resource
    private NewsIndexService service;


    @Resource
    private NewsService newsService;

    @Resource
    private ContentIndexMapper mapper;


    @Resource
    private NewsMapper mappers;
    @Autowired
    private NewsMapper newsMapper;

    @Resource
    private NewsIndexService services;

    @Test
    public void contextLoads() {
        Long[] ids = {1L};
        /*属性拷贝*/
        List<News> news = newsMapper.findAll();
        System.out.println(news);
        List<NewsIndex> newsIndex = new ArrayList<>();

        news.forEach(news1 -> {
            NewsIndex index = new NewsIndex();
            BeanUtils.copyProperties(news1, index);
            newsIndex.add(index);
        });


    }


    @Test
    public void test() {
        services.matchNewsContent("总书记");
    }


    @Test
    public void test1() {
        /*属性拷贝*/
        News news = newsMapper.findById(1L).get();
        NewsIndex index = new NewsIndex();
        BeanUtils.copyProperties(news, index);
        index.setNewsTitle("测试测试测试测试测试测试测试");
        System.out.println(index);
        mapper.save(index);
    }


    @Test
    public void test2() {
//        newsService.getNewsByIds(new Long[]{5L, 8L});
        log.info(newsService.queryNewsById(5L).toString());
    }
}
