package com.site.springboot.core.dao;


import com.site.springboot.core.entity.NewsIndex;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ContentIndexMapper extends ElasticsearchRepository<NewsIndex, Long> {

    @Highlight(fields = {
            @HighlightField(name = "newsContent")
    })
    List<NewsIndex> findByNewsContentContains(String newsContent, Pageable pageable);
}

