package com.site.springboot.core.service;

import com.site.springboot.core.entity.NewsIndex;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsIndexService {

    public PageResult getNewsIndexByContent(String content, PageQueryUtil page);

    public void matchNewsContent(String content);
}
