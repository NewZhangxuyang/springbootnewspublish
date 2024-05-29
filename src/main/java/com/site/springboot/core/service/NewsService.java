package com.site.springboot.core.service;

import com.site.springboot.core.entity.News;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;

import java.util.List;

public interface NewsService {
    String saveNews(News news);

    PageResult getNewsPage(PageQueryUtil pageUtil);

    Boolean deleteBatch(Long[] ids);

    List<News> getNewsByIds(Long [] ids);


    /**
     * 根据id获取详情
     *
     * @param newsId
     * @return
     */
    News queryNewsById(Long newsId);

    /**
     * 后台修改
     *
     * @param news
     * @return
     */
    String updateNews(News news);



    /*
     *增加浏览量
     **/

    void addViews(Long newsId);


}
