package com.site.springboot.core.service.impl;

import com.site.springboot.core.config.Constants;
import com.site.springboot.core.dao.NewsMapper;
import com.site.springboot.core.entity.News;
import com.site.springboot.core.entity.NewsComment;
import com.site.springboot.core.service.NewsService;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public String saveNews(News news) {
        news.setIsDeleted(Constants.Delete_Flag_Exist);
        news.setNewsViews(0L);
        News new1 = newsMapper.save(news);
        if (!Objects.isNull(new1.getNewsId())) {
            return "success";
        }
        return "保存失败";
    }

    @Override
    public PageResult getNewsPage(PageQueryUtil pageUtil) {
        Pageable pageRequest = PageRequest.of(pageUtil.getPage() - 1, pageUtil.getLimit());
        List<News> news = newsMapper.findAllByIsDeleted(Constants.Delete_Flag_Exist, pageRequest).getContent();
        int total = newsMapper.countAllByIsDeleted(Constants.Delete_Flag_Exist);
        return new PageResult(news, total, pageUtil.getLimit(), pageUtil.getPage());
    }


    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        List<News> lists = newsMapper.findAllById(Arrays.asList(ids)).stream().peek(news -> {
            news.setIsDeleted(Constants.Delete_Flag_Delete);
        }).toList();
        newsMapper.saveAll(lists);
        //删除分类数据
        return true;
    }

    @Override
    public News queryNewsById(Long newsId) {
        return newsMapper.findNewByNewsId(newsId);
    }

    @Override
    public String updateNews(News news) {
        News newsForUpdate = newsMapper.findNewByNewsId(news.getNewsId());
        if (newsForUpdate == null) {
            return "数据不存在";
        }
        news.setNewsCategoryId(news.getNewsCategoryId());
        news.setNewsContent(news.getNewsContent());
        news.setNewsCoverImage(news.getNewsCoverImage());
        news.setNewsStatus(news.getNewsStatus());
        news.setNewsTitle(news.getNewsTitle());
        news.setUpdateTime(new Date());
        if (!Objects.isNull(newsMapper.save(news))) {
            return "success";
        }
        return "修改失败";
    }
}
