package com.site.springboot.core.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.site.springboot.core.config.Constants;
import com.site.springboot.core.dao.ContentIndexMapper;
import com.site.springboot.core.entity.News;
import com.site.springboot.core.entity.NewsIndex;
import com.site.springboot.core.service.NewsIndexService;
import com.site.springboot.core.util.PageQueryUtil;
import com.site.springboot.core.util.PageResult;
import jakarta.annotation.Resource;
import org.ehcache.shadow.org.terracotta.context.query.QueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NewIndexServiceImpl implements NewsIndexService {
    @Resource
    private ContentIndexMapper mapper;


    @Resource
    private ElasticsearchClient client;

    @Override
    public PageResult getNewsIndexByContent(String content, PageQueryUtil page) {
        Pageable pageRequest = PageRequest.of(page.getPage() - 1, page.getLimit());
        List<NewsIndex> news = mapper.findByNewsContentContains(content, pageRequest);
        List<NewsIndex> list = news.stream().filter(newsIndex -> newsIndex.getIsDeleted() == 0).toList();
        int total = (int) mapper.count();
        return new PageResult(list, total, page.getLimit(), page.getPage());
    }

    public void matchNewsContent(String content)  {
        SearchResponse<NewsIndex> sea = null;
        try {
            sea = client.search(search -> search.index("detail_search")
                    .query(query -> query.match(match -> match.field("newsContent").query(content))
                    ), NewsIndex.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sea.hits().hits().forEach(System.out::println);
    }
}
