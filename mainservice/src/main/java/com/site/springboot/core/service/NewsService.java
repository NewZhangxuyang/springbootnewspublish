package com.site.springboot.core.service;

import com.site.springboot.core.entity.News;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;

import java.util.List;

public interface NewsService {
    News saveNews(News news);

    PageResult getNewsPage(PageQueryUtil pageUtil);

    List<News> deleteBatch(Long[] ids);

    List<News> getNewsByIds(Long[] ids);

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
    News updateNews(News news);

    /*
     *增加浏览量
     **/
    News addViews(Long newsId);


    Boolean praiseNews(Long newsId, String userName);


    public int selectIsPraise(Long newsId, String userName);

    Boolean UnPraiseNews(Long newsId, String user);

    public List<String> getTotalPraiseUser(Long newsId);

    public int getTotalPraiseCount(Long newsId);

}
