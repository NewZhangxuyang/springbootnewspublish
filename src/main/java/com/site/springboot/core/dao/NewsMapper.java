package com.site.springboot.core.dao;

import com.site.springboot.core.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface NewsMapper extends JpaRepository<News, Long>, QuerydslPredicateExecutor<News> {

    News findNewByNewsId(Long newsId);

    int countAllByIsDeleted(Byte isDeleted);

    Page<News> findAllByIsDeleted(Byte isDeleted, Pageable page);
}