package com.site.springboot.core.service.impl;

import com.site.springboot.core.config.Constants;
import com.site.springboot.core.dao.AdminMapper;
import com.site.springboot.core.dao.ContentIndexMapper;
import com.site.springboot.core.dao.NewsMapper;
import com.site.springboot.core.entity.News;
import com.site.springboot.core.entity.NewsIndex;
import com.site.springboot.core.service.NewsService;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import com.site.springboot.core.util.RedisUtil;
import com.site.springboot.core.util.annotation.RedisCacheDel;
import com.site.springboot.core.util.annotation.RedisCacheGet;
import com.site.springboot.core.util.annotation.RedisCacheSet;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    @Resource
    private ContentIndexMapper mapper;


    @Resource
    private RedisUtil redis;

    @Resource
    private AdminMapper admin;
    @Autowired
    private AdminMapper adminMapper;

    @RedisCacheSet(retype = "single")
    @Override
    public News saveNews(News news) {
        news.setIsDeleted(Constants.Delete_Flag_Exist);
        news.setNewsViews(0L);
        News newData = newsMapper.save(news);
        if (!Objects.isNull(newData.getNewsId())) {
            NewsIndex newsIndex = new NewsIndex();
            BeanUtils.copyProperties(newData, newsIndex);
            mapper.save(newsIndex);
            return newData;
        }
        return null;
    }


    @RedisCacheGet(retype = "list")
    public List<News> getNewsByIds(Long[] ids) {
        return newsMapper.findAllById(Arrays.asList(ids));
    }


    @Override
    public PageResult getNewsPage(PageQueryUtil pageUtil) {
        Pageable pageRequest = PageRequest.of(pageUtil.getPage() - 1, pageUtil.getLimit());
        List<News> news = newsMapper.findAllByIsDeleted(Constants.Delete_Flag_Exist, pageRequest).getContent();
        int total = newsMapper.countAllByIsDeleted(Constants.Delete_Flag_Exist);
        return new PageResult(news, total, pageUtil.getLimit(), pageUtil.getPage());
    }


    @RedisCacheDel(retype = "list")
    @Override
    public List<News> deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return null;
        }
        List<NewsIndex> newsIndex = new ArrayList<>();
        List<News> lists = newsMapper.findAllById(Arrays.asList(ids)).stream().peek(news -> {
            news.setIsDeleted(Constants.Delete_Flag_Delete);
            NewsIndex index = new NewsIndex();
            BeanUtils.copyProperties(news, index);
            newsIndex.add(index);
        }).toList();
        List<News> newsList = newsMapper.saveAll(lists);
        if (!newsList.isEmpty()) {
            mapper.deleteAll(newsIndex);
            return newsList;
        }
        return null;
    }

    @RedisCacheGet(retype = "single")
    @Override
    public News queryNewsById(Long newsId) {
        return newsMapper.findNewByNewsId(newsId);
    }


    @RedisCacheSet(retype = "single")
    @Override
    public News updateNews(News news) {
        News newsForUpdate = newsMapper.findNewByNewsId(news.getNewsId());
        if (newsForUpdate == null) {
            return null;
        }
        news.setNewsCategoryId(news.getNewsCategoryId());
        news.setNewsContent(news.getNewsContent());
        news.setNewsCoverImage(news.getNewsCoverImage());
        news.setNewsStatus(news.getNewsStatus());
        news.setNewsTitle(news.getNewsTitle());
        news.setUpdateTime(new Date());
        news.setCreateTime(newsForUpdate.getCreateTime());
        news.setIsDeleted(newsForUpdate.getIsDeleted());
        news.setNewsViews(newsForUpdate.getNewsViews());
        News updateNews = newsMapper.save(news);
        if (!Objects.isNull(updateNews.getNewsId())) {
            NewsIndex newsIndex = new NewsIndex();
            BeanUtils.copyProperties(news, newsIndex);
            mapper.save(newsIndex);
            return updateNews;
        }
        return null;
    }


    @RedisCacheSet(retype = "single")
    @Override
    public News addViews(Long newsId) {
        News news = newsMapper.findNewByNewsId(newsId);
        if (news != null) {
            news.setNewsViews(news.getNewsViews() + 1);
            newsMapper.save(news);
            NewsIndex newsIndex = new NewsIndex();
            BeanUtils.copyProperties(news, newsIndex);
            mapper.save(newsIndex);
        }
        return news;
    }


    public Boolean praiseNews(Long newsId, String userName) {
        String praiseKey = "praise" + newsId;
        String praiseValue = userName + "::" + newsId;
        String totalKey = "praiseTotal" + newsId;
        if (redis.sHasKey(praiseKey, praiseValue)) {
            return false;
        }
        redis.sSet(praiseKey, praiseValue);
        redis.incr(totalKey, 1);
        return true;
    }

    public int selectIsPraise(Long newsId, String userName) {
        String praiseKey = "praise" + newsId;
        String praiseValue = userName + "::" + newsId;
        if (redis.hasKey(praiseKey) && redis.sHasKey(praiseKey, praiseValue)) {
            return 1;
        }
        return 0;
    }

    @Override
    public Boolean UnPraiseNews(Long newsId, String userName) {
        String praiseKey = "praise" + newsId;
        String praiseValue = userName + "::" + newsId;
        String totalKey = "praiseTotal" + newsId;
        if (redis.hasKey(praiseKey) && redis.sHasKey(praiseKey, praiseValue)) {
            redis.setRemove(praiseKey, praiseValue);
            redis.decr(totalKey, 1);
        }
        return false;
    }

    public List<String> getTotalPraiseUser(Long newsId) {
        String praiseKey = "praise" + newsId;
        Set set = redis.sGet(praiseKey);
        List<String> names = set.stream().map(o -> {
            String[] strings = o.toString().split("::");
            return strings[0];
        }).toList();
        return names;
    }

    public int getTotalPraiseCount(Long newsId) {
        String totalKey = "praiseTotal" + newsId;
        return redis.get(totalKey) == null ? 0 : Integer.parseInt(redis.get(totalKey).toString());
    }


}
